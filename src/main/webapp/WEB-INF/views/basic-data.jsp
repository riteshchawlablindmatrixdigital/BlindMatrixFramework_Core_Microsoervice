<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.1/css/dataTables.dataTables.css">
    <script type="text/javascript"
            src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/2.3.1/js/dataTables.js"></script>
    <script src="jquery.progressScroll.min.js"></script>

    <style type="text/css">
        .center {
            margin: auto;
            width: 100%;
            border: 2px solid green;
            padding: 10px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="center">
    <h2><font color="blue">Fiserv WiseShip Data Application :: Snowflake Data Reports</font></h2>
    <h2>${requestData.query}</h2>
    <h2>${requestData.numberOfResults}</h2>
    <form:form method="POST"
               action="/basic-data-details" modelAttribute="requestData">
        <table align="center">
            <tr>
                <td valign="top">
                    <form:label path="query" title="Snowflake SQL Query:"/>
                    <form:textarea id="query" path="query" value="query" name="query" rows="8" cols="90"/>
                </td>
                <td valign="top">
                    <form:label path="numberOfResults" title="Number of Results: LIMIT Optional"/>
                    <form:input type="text" path="numberOfResults"  value="numberOfResults" id="numberOfResults" name="numberOfResults" width="100%"
                                height="äuto"></form:input>
                    <br/></td>
                <td valign="top"><input type="submit" class="button-submit" id="btnSubmit"
                                        name="btnSubmit" value="Submit Query - Get Snowflake Data"/></a></td>
            </tr>
        </table>
    </form:form>
</div>

<pre>
    <table id="example" class="display nowrap cell-border compact hover order-column row-border stripe"
           cellspacing="0" width="100%" height="auto">
    </table>
</pre>

<script type="text/javascript">
    function submitQueryAndDataTableResults() {
        $("#btnSubmit").prop("disabled", true);
        var dataObject = [];
        $.ajax({
            type: "POST",
            url: "/fiserv/white-data/apis/execute-query/results",
            dataType: "json",
            data: "{ \"query\"" + " : \"" + $("#query").val() + "\" , \"numberOfResults\" : \"" + $("#numberOfResults").val() + "\" }",
            contentType: 'application/json; charset=utf-8',
            processing: true,
            success: function (response) {
                dataObject = eval(response);
                $('#example').dataTable({
                    searching: true,
                    columnDefs: [
                        {className: "dt-body-left", targets: "_all"},
                        {className: "dt-head-left", targets: "_all"},
                    ],
                    columns: dataObject.headers,
                    dataType: "json",
                    cache: false,
                    timeout: 600000,
                    scrollY: true,
                    scrollX: true,
                    paging: true,
                    processing: true,
                    ajax: {
                        url: '/fiserv/white-data/apis/api/getData',
                        contentType: 'application/json',
                        dataType: "json",
                        type: 'POST',
                        processData: true
                    }
                });
                $("#btnSubmit").prop("disabled", false);
            }
        });
        console.log('DTB DONE DTB !!!!!!!!!!!!!!!!!!!!!!!!!!!!  ::::  ');
    }
    console.log('${requestData.query}');
    $(document).ready(function () {
        $("#btnSubmit").click(function (event) {
            event.preventDefault();
            submitQueryAndDataTableResults();
        });
    });

</script>

</body>
</html>