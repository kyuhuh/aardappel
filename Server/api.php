<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>    
    <script src="../<path_to_jquery>/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        var uri = '/api/vs';
        $(document).ready(function () {
            $.getJSON(uri)
            .done(function (data) {
                alert('got: ' + data);
            });

            $.ajax({
                url: '/api/vs/5',
                async: true,
                success: function (data) {
                    alert('seccess1');
                    var res = parseInt(data);
                    alert('got res=' + res);
                }
            });
        });
    </script>
</head>
<body>
....
</body>
</html>