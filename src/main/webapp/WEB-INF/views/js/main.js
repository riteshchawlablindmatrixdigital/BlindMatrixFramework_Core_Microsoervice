$(document).ready(function () {

    $("#btnSubmit").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

    fire_ajax_submit();
});

function fire_ajax_submit() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    data.append("CustomField", "This is some extra data, testing");

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/api/upload/multi",
        data: data,
        //http://api.jquery.com/jQuery.ajax/
        //https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (dataSet) {

            var my_columns = [];

            $.each( dataSet[0], function( key, value ) {
                var my_item = {};
                my_item.data = key;
                my_item.title = key;
                my_columns.push(my_item);
            });

            $('#example').DataTable({
                data: dataSet,
                "columns": my_columns
            });

            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);

//            $("#result").text(data);
        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });


}
