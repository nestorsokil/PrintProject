package com.myproject.sample.xmlmodel;

import com.myproject.sample.canvas.ICanvas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.File;


@XmlAccessorType(XmlAccessType.FIELD)
public class TextXml extends AbstractXmlElement{

    @XmlAttribute(name = "value")
    protected String value;

    @XmlAttribute(name = "font-size")
    protected int fontSize;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override public void drawOnCanvas(ICanvas canvas, File projectFolder) {
        canvas.drawText(this);
    }
}
