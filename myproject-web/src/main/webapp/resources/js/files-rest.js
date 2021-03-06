$('document').ready(function(){
    if(!!document.getElementById('files'))
        getAllFiles(0, 5);
    document.getElementById('uploadInput').onchange = getFilename;
    $('#upload').on('submit', postFile);
});

var postFile = function(){
    $('#upload').ajaxSubmit({
        url: 'rest/upload',
        type: 'post',
        success: function(response){
            console.log(response);
            var act = $('.getPage.activated');
            var activePage = $('#pages').index(act) + 1;
            getAllFiles(activePage, 5);
            },
        error: function(e){
            confirm("Error processing project");
        }});
    return false;
}

var getAllFiles = function(page, size){
    $('.getPage.activated').removeClass('activated');
    var list = '<ul>';
    $.get('rest/download?page='+page+'&size='+size, function( response ){
        $.each(response, function(i, project){
            list += '<li><img style="border: 1px solid black;" width="100" height="100" src="data:image/png;base64,'
                                               + project.thumbnail + '" /></li>'
                + project.name + '<br />'
                + '<a href="rest/download/' + project.id + '">Download</a><br />'
                + '<a href="rest/delete/'   + project.id + '">Delete</a><hr align="left" width="200" />';
                });

        list += '</ul>';

        $('ul').remove();
        $('#files').append(list);

        displayPageNumbers(page);
     });
}

var displayPageNumbers = function(activePage){
    getProjectCount(function(count){
        var content = "";
        for(var i=0; i<(count/5); i++)
            content += '<a href="#" class="getPage" onClick="getAllFiles('+i+',5)">' + (i+1) + '</a><nobr />';
        $('#pages a').remove();
        $('#pages').append(content);
        $('.getPage').eq(activePage).addClass('activated');
    });
}

var getProjectCount = function(callback){
    $.get('rest/download/count', function(response){
        callback(response);
    });
}

var getFilename = function(){
    var name =  document.getElementById('uploadInput').value;
    document.getElementById('uploadFileName').value = name;
}