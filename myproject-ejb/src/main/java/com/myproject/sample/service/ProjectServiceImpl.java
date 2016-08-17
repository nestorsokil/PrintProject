package com.myproject.sample.service;

import com.myproject.sample.exception.UnsuccessfulProcessingException;
import com.myproject.sample.model.Project;
import com.myproject.sample.model.Storage;
import com.myproject.sample.model.User;
import com.myproject.sample.processor.Processor;
import com.myproject.sample.processor.ProjectProcessor;
import com.myproject.sample.util.ProjectFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class ProjectServiceImpl extends GenericServiceImpl<Project> implements ProjectService {

    @Inject private StorageService storageService;

    @Inject @Processor("Basic") private ProjectProcessor processor;

    @Override
    public String saveProject(User uploader, InputStream fileStream, String fileName)
            throws UnsuccessfulProcessingException {
        Storage userStorage = storageService.getStorageById("storage_user");
        Project project = new Project();
        project.setName(FilenameUtils.getBaseName(fileName) + "("
                + DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss") + ")");
        project.setUser(uploader);
        project.setStorage(userStorage);
        project = this.update(project);

        String projectPath = userStorage.getPath() + File.separator + project.getId();
        File projectFolder = new File(projectPath);

        if(!projectFolder.mkdir())
            throw new UnsuccessfulProcessingException("Could not create folder");

        try {
            ProjectFileUtils.unzipProject(fileStream, projectPath);
        }catch (IOException ioe){
            projectFolder.delete();
            this.delete(project);
            throw new UnsuccessfulProcessingException(ioe);
        }

        processor.process(project);
        return projectPath;
    }

    //async call to MDB
    //just for training, because of using REST calls processing is already asynchronous

    @Resource(name = "myProjectConnectionFactory") private ConnectionFactory connectionFactory;
    @Resource(name = "fileUploadQueue") private Destination destination;

    private void invokeMdbProcessor(Project project){
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage(project);
            producer.send(message);
            session.close();
            connection.close();
        }catch (JMSException je){je.printStackTrace();}
    }
}
