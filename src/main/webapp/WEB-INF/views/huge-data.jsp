<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html xmlns:th="https://www.thymeleaf.org">
<head>
    <!--<meta name="viewport" content="width=device-width" />-->
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.1/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/scroller/2.4.3/css/scroller.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/keytable/2.12.1/css/keyTable.dataTables.css">
    <!--<link rel="stylesheet" href="https://cdn.datatables.net/responsive/3.0.4/css/responsive.dataTables.min.css">-->


    <script type="text/javascript"
            src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/2.3.1/js/dataTables.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/scroller/2.4.3/js/dataTables.scroller.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/scroller/2.4.3/js/scroller.dataTables.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/keytable/2.12.1/js/dataTables.keyTable.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/keytable/2.12.1/js/keyTable.dataTables.js"></script>
    <!--    <script type="text/javascript"
                src="https://cdn.datatables.net/responsive/3.0.4/js/dataTables.responsive.min.js"></script>-->
    <style type="text/css">

        .center {
            margin: auto;
            width: 100%;
            border: 2px solid green;
            padding: 10px;
            text-align: center;
        }

        .button-submit:hover {
            color: white;
        }
    </style>
</head>
<body>
<div class="center">
    <h2><font color="blue">Blank Matrix Data Systems Application :: Snowflake Data Reports</font></h2>
    <h2>${blankMatrixSystemsRequestData.query}</h2>
    <h2>${blankMatrixSystemsRequestData.numberOfResults}</h2>
    <form:form method="POST"
               action="/huge-data-details" modelAttribute="blankMatrixSystemsRequestData">
        <table align="center">
            <tr>
                <td valign="top">
                    <label path="query" title="Snowflake SQL Query:"/>
                    <form:textarea id="query" path="query" value="query" name="query" rows="8" cols="90"/>
                </td>
                <td valign="top">
                    <form:label path="numberOfResults" title="Number of Results: LIMIT Optional"/>
                    <form:input type="text" path="numberOfResults"  value="numberOfResults" id="numberOfResults" name="numberOfResults" width="100%"
                                height="äuto"></form:input>
                    <br/></td>
                <td valign="top"><input type="submit" class="button-submit" value="Submit Query - Get Snowflake Data"/></td>
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
    console.log('${blankMatrixSystemsRequestData.query}');
    function submitQueryAndDataTableResults() {
        $("#btnSubmit").prop("disabled", true);
        var dataObject = [];
        $.ajax({
            type: "POST",
            url: "/blank-matrix-systems/apis/execute-query/results",
            dataType: "json",
            data: "{ \"query\"" + " : \"" + $("#query").val() + "\" , \"numberOfResults\" : \"" + $("#numberOfResults").val() + "\" }",
            contentType: 'application/json; charset=utf-8',
            success: function (response) {
                dataObject = eval(response);
                new DataTable('#example', {
                    searching: true,
                    columnDefs: [
                        {className: "dt-body-left", targets: "_all"},
                        {className: "dt-head-left", targets: "_all"},
                    ],
                    columns: dataObject.headers,
                    dataType: "json",
                    serverSide: true,
                    cache: false,
                    timeout: 600000,
                    layout: {
                        topStart: null,
                        topEnd: 'paging'
                    },
                    scrollCollapse: true,
                    scroller: true,
                    scrollY: 400,
                    paging: true,
                    ajax: {
                        url: '/blank-matrix-systems/apis/getQueryData',
                        contentType: 'application/json',
                        dataType: "json",
                        type: 'POST',
                        data: function (data, callback, settings) {
                            setTimeout(() => {
                                callback({
                                    draw: data.draw,
                                    data: data.data,
                                    recordsTotal: data.recordsTotal,
                                    recordsFiltered: data.recordsFiltered
                                });
                            }, 150);
                        },
                    }
                });
            }
        });
        console.log('DTB DONE DTB !!!!!!!!!!!!!!!!!!!!!!!!!!!!  ::::  ');
        $("#btnSubmit").prop("disabled", false);
    }

    $(document).ready(function () {
        $("#btnSubmit").click(function (event) {
            event.preventDefault();
            submitQueryAndDataTableResults();
        });
    });

</script>

</body>
</html>