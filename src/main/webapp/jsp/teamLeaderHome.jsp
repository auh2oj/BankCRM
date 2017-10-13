<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.bank.crm.entity.form.UserForm"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-2.2.4.min.js">
</script>

<script>
var contextPath = "${pageContext.request.contextPath}";

/**
 * Function that renders the online Agents and accepted Customers by
 submitting an AJAX request to the server.
 */
$(document).ready(function(){
	
	var purl ="teamLeaderHomeAgents";
	$.ajax({
			url : purl,
			type : 'GET',
			dataType : 'json',
			contentType : 'application/json',
			mimeType : 'application/json',
			success : function(jsonData) {
				alert(jsonData);
				console.log(jsonData);
				for (var i = 0; i < jsonData.length; i++) {
					showOnlineAgents(jsonData[i]);
					$("#chooseAgent").append("<option>"+jsonData[i]+"</option>")
				}
			},
			error : function(data, status, er) {
				alert("error: " + data + " status: " + status + " er:" + er);
				console.log(data);
			}
		}

	);//end of ajax call
	
	var qurl = "teamLeaderHomeCustomers";
	$.ajax({
		url : qurl,
		type : 'GET',
		dataType : 'json',
		contentType : 'application/json',
		mimeType : 'application/json',
		success : function(jsonData) {
			alert(jsonData);
			console.log(jsonData);
			for (var i = 0; i < jsonData.length; i++) {
				showAcceptedCustomers(jsonData[i]);
				$("#chooseCustomer").append("<option>"+jsonData[i].cid+"</option>")
				//customerList.push(jsonData[i]);
			}
		},
		error : function(data, status, er) {
			alert("error: " + data + " status: " + status + " er:" + er);
			console.log(data);
		}
	}

	);//end of ajax call
	
	var rurl = "teamLeaderHomeAssignments";
	$.ajax({
		url : rurl,
		type : 'GET',
		dataType : 'json',
		contentType : 'application/json',
		mimeType : 'application/json',
		success : function(jsonData) {
			alert(jsonData);
			console.log(jsonData);
			for (var i = 0; i < jsonData.length; i++) {
				showAssignments(jsonData[i]);
			}
		},
		error : function(data, status, er) {
			alert("error: " + data + " status: " + status + " er:" + er);
			console.log(data);
		}
	}

	);//end of ajax call
});

	function assign() {
		var agent = $("#chooseAgent option:selected").val();
		var cid = $("#chooseCustomer option:selected").val();
		var purl = "teamLeaderHomeAssign/" + agent + "/" + cid;
		console.log("here");
		$.ajax({
			url : purl,
			type : 'PUT',
			dataType : 'json',
			contentType : 'application/json',
			mimeType : 'application/json',
			data : {},
			success : function(jsonData) { //data= this.responseText
				//data is JavaScript object against JSON response coming fromm the server
				console.log("jsonData.status " + jsonData.status);
				if (jsonData.status == "success") {
					console.log("success");
				} else {
					alert("Sorry! data could not be updated");
				}
			}
		});
		window.location.reload();
	}

	function showOnlineAgents(agentName) {
		var table = document.getElementById("activeAgentsTable");
		
		var tr = document.createElement("tr");
		
		generateCell(tr, agentName);
		
		table.appendChild(tr);
	}
	
	//var customerFields = ["Name", "Address", "Phone", "Age", "Loan Amount", "SSN"];
	function showAcceptedCustomers(customer) {
		var table = document.getElementById("acceptedCustomersTable");
		
		var tr = document.createElement("tr");
		
		generateCell(tr, customer.cid);
		generateCell(tr, customer.name);
		generateCell(tr, customer.address);
		generateCell(tr, customer.contact);
		generateCell(tr, customer.age);
		generateCell(tr, customer.loan);
		generateCell(tr, customer.ssn);
		
		table.appendChild(tr);
	}
	
	function showAssignments(assignment) {
		var table = document.getElementById("assignmentsTable");
		
		var tr = document.createElement("tr");
		
		generateCell(tr, assignment.agentUsername);
		generateCell(tr, assignment.customer.cid);
		generateCell(tr, assignment.status);
		
		table.appendChild(tr);
	}
	
	function generateCell(tr, txt) {
		var td = document.createElement("td");
		var txt = document.createTextNode(txt);
		
		td.appendChild(txt);
		tr.appendChild(td);
	}

</script>

</head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
</head>
<body>
	Hello Team Leader!
	<!-- Show online agents here -->
	<div id="activeAgents">
		<table id="activeAgentsTable" class="table table-bordered" style="width: 50%;">
        <thead>
            <tr style="background-color: rgba(1, 171, 114, 0.18);">
                <th>Online Agents:</th>
            </tr>
        </thead>
        
        <tbody>
        </tbody>
    		</table>
    		<br/><br/><br/><br/>
	</div>
	
	
	<!-- Assign Customers to Agents -->
	<div id="agentCustomerAssign">
		<select id="chooseAgent"></select>
		<select id="chooseCustomer"></select>
		<button class="btn btn-danger" id="Button" onclick="assign()">Assign</button>
	</div>
	
	
	<!-- Show accepted customers here -->
	<div id="acceptedCustomers">
		<table id="acceptedCustomersTable" class="table table-bordered" style="width: 90%;">
		<thead>
			<tr style="background-color: rgba(1, 171, 114, 0.18);">
				<th>Accepted Customers:</th>
			</tr>
		</thead>
		<thead>
			<tr style="background-color: rgba(12, 200, 160, 1);">
				<th>CID</th>
				<th>Name</th>
				<th>Address</th>
                <th>Phone</th>
                <th>Age</th>
                <th>Loan Amount</th>
                <th>SSN</th>
			</tr>
		</thead>
        <tbody>
        </tbody>
		</table>
		<br/><br/><br/><br/>
	</div>
	
	<!-- Show Agent-Customer assignments here -->
	<div id="agentCustomerAssignments">
		<table id="assignmentsTable" class="table table-bordered" style="width: 25%;">
		<thead>
			<tr style="background-color: rgba(1, 171, 114, 0.18);">
				<th>Agent-Customer Assignments:</th>
			</tr>
		</thead>
		<thead>
			<tr style="background-color: rgba(12, 200, 160, 1);">
				<th>Agent Name</th>
				<th>Customer ID</th>
				<th>Agent Status</th>
			</tr>
		</thead>
        <tbody>
        </tbody>
		</table>
		<br/><br/><br/><br/>
	</div>
</body>
</html>