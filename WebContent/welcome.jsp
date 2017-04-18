<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet"href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<link href="styles/jquery-ui.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
			
<title>SearchEngine</title>

<script>
$(document).ready(function() {
	$(function() {
	$("#datepickerI").datepicker({"dateFormat": "dd/mm/yy"});

	$("#datepickerII").datepicker({"dateFormat": "dd/mm/yy"});

	});
	$('#submit').click(function(event) {
	var queryterm = $('#query').val();
	var beginDate = $('#datepickerI').val();
	var endDate =  $('#datepickerII').val();
	if (queryterm.length < 1) {
		alert("Please type some text.");
	}else{
	$.get('DoServlet',
	{
		queryTerm : queryterm,
		beginDate : beginDate,
		endDate   : endDate
	},function(responseText) {
	document.getElementById("search_result").innerHTML = responseText;
	$("#search_result").show();
	});//
}
});
});
</script>
</head>


<body>
<div class="container">
			<table class="table-responsive">
			<tr>
				<td> <input type="text" placeholder="Enter your query" class="form-control input-lg" name="query" id="query" size="40" autocomplete="on" required> </input> </td> 
				<td> <input type="button" class="btn btn-lg btn-primary" id="submit" value="Search"> </input> </td>
			</tr>
	<p></p>
			<tr>
			<table>
			<tr>
			<div class="col-xs-0">
				<td> <strong class="text-danger">From:</strong><input type="text" class="form-control input-sm"  id="datepickerI" size="10"> </input> </td> 				
				<td> <strong class="text-danger">To:</strong>  <input type="text" class="form-control input-sm" id="datepickerII" size="10"> </input> </td>
			</div>
		   </tr>
		   </table>
		   </table>
	
   
   
   				<div id="search_result"></div>

   
   
   
</body>

</html>