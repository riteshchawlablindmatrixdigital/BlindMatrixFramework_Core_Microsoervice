<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<head>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.1/css/dataTables.dataTables.css">
    <link href="css/jquery.shCircleLoader.css?${a}" rel="stylesheet"/>
    <script type="text/javascript"
            src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/2.3.1/js/dataTables.js"></script>
    <script type="text/javascript"
            src="js/jquery.shCircleLoader-min.js?${a}"></script>

    <style type="text/css">
        .center {
            margin: auto;
            width: 100%;
            border: 2px solid green;
            padding: 10px;
            text-align: center;
        }

        .myns > div {
            box-shadow: 0 0 6px black, inset 0 0 6px black;
        }
    </style>
</head>
<body>
<div class="center">
    <h2><font color="blue">Blank Matrix Data Systems Application :: Snowflake Data Reports</font></h2>
    <%--<h2>${blankMatrixSystemsRequestData.query}</h2>
        <h2>${blankMatrixSystemsRequestData.numberOfResults}</h2>--%>
    <form:form id="theFormSpring" method="POST"
               action="/basic-data-details" modelAttribute="blankMatrixSystemsRequestData">
        <table align="center">
            <tr>
                <td valign="top">
                    <font color="blue">Snowflake SQL Query:</font>
                    <form:textarea id="query" path="query" value="query" name="query" rows="8" cols="90"/>
                </td>
                <td valign="top">
                    <font color="blue">Number of Results: LIMIT Optional</font>
                    <form:input type="text" path="numberOfResults" id="numberOfResults" name="numberOfResults"
                                width="100%"
                                height="äuto"></form:input>
                    <br/></td>
                <td valign="top"><input type="submit" id="theForm" name="theForm" class="button-submit"
                                        value="Submit Query - Get Snowflake Data"/></td>
            </tr>
        </table>
    </form:form>
</div>
<div id="loader"></div>
<div id="errorDiv"><font color="red">${error}</font></div>
<div id="resultsDiv"><font color="green">${NoResultsFound}</font></div>
<div>
    <table id="example" class="display nowrap cell-border compact hover order-column row-border stripe"
           cellspacing="0" width="100%" height="auto">
    </table>
</div>

<script type="text/javascript">
    function submitQueryAndDataTableResults() {
        $("#theForm").prop("disabled", true);
        $('#errorDiv').innerHTML = "";
        $('#resultsDiv').innerHTML = "";
        var dataObject = eval(${resultData});
        $('#example').dataTable({
            searching: true,
            columnDefs: [
                {width: "50px", targets: "_all"},
                {className: "dt-body-left", targets: "_all"},
                {className: "dt-head-left", targets: "_all"}
            ],
            columns: dataObject.headers,
            dataType: "json",
            cache: false,
            timeout: 600000,
            scrollY: true,
            scrollX: true,
            paging: true,
            processing: true,
            processData: true,
            data: dataObject.queryData.data
        });
        $("#theForm").prop("disabled", false);
        $('#loader').css('display', 'none');
    }
    console.log('DTB DONE DTB !!!!!!!!!!!!!!!!!!!!!!!!!!!!  ::::  ');
    console.log('${error}');
    $(document).ready(function () {
        $("#theForm").click(function (event) {
            document.getElementById("theFormSpring").submit();
        });
        $("#theForm").prop("disabled", true);
        $('#loader').css('display', 'block');
        $('#loader').shCircleLoader({clockwise: false});
        if('${blankMatrixSystemsRequestData.query}' != "") {
            if('${NoResultsFound}' != 'No Data Available for the Query') {
                submitQueryAndDataTableResults();
            }
        }
        $("#theForm").prop("disabled", false);
        $('#loader').css('display', 'none');
    });
</script>

</body>
</html>