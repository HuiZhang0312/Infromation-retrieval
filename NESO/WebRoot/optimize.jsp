<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ page language="java" import="search.*"%>
<%@ page language="java" import="classes.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	List<Question> qList = (List)request.getAttribute("qList");
	
	String query = (String)request.getSession().getAttribute("query");
	
	String originLanguage = (String)request.getSession().getAttribute("originLanguage");
	
	String search = (String) request.getSession().getAttribute("search");
	
	Translator t = new Translator();
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>Search result</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
	<link rel="stylesheet" type="text/css" href="css/style.css">

	<style type="text/css">

* {
    margin:0;
    padding:0;
}

a {
    color:#b7b1a1;
    text-decoration:none;    
}

a:hover {
    color:#1E7ACE;
}

input, select,textarea {
    padding:1px;
    margin:2px;
    font-size:11px;
}
input.button {
	display: inline-block;
	outline: none;
	cursor: pointer;
	text-align: center;
	font: 14px/100% Arial, Helvetica, sans-serif;
	padding: .5em 2em .55em;
	-webkit-border-radius: .5em; 
	-moz-border-radius: .5em;
	border-radius: .5em;

}
input.button:hover {
	background: #f47c20;
}
input.button:active {
	position: relative;
	top: 1px;
}
#formwrapper {
    width:450px;
    margin:15px auto;
    padding:20px;
    text-align:center;
    border:1px #1E7ACE solid;
}

fieldset {
    padding:10px;
    margin-top:5px;
    border:1px solid #1E7ACE;
    background:#fff;
}


fieldset label {
    float:left;
    width:120px;
    text-align:right;
    padding:4px;
    margin:1px;
}

fieldset div {
    clear:left;
    margin-bottom:2px;
}

fieldset div .enter{ text-align:center;}
.clear {
    clear:both;
}
select {
	width:145px;
}
.search{
	width:400px;
	height:40px;
}

</style>
	</head>

	<body>
	<div id="header">
  		<ul>
			<li class="selected"><a href="index.jsp">home</a></li>
			<li><a href="about.html">about</a></li>
			<li><a href="blog.html">blog</a></li>
			<li><a href="contact.html">contact us</a></li>
		</ul>
  		<div class="logo">
			<a href="index.jsp"><img src="images/logo.gif" alt="" /></a>
		</div>
	</div>
	<div id="body">
		<div class="about">
		<h1>
			Non English Stack Overflow
		</h1>
		<form action="SearchServlet" method="post">
			<fieldset>

				<p>
					<strong> Search what you want. </strong>
				</p>
				<div class="enter">
					<table>
						<tr>
							<td width="400px">
								<input type="text" class="search" id="search" name="search" value="<%=query %>"/>
							</td>
							<td>
								<input type="submit" class="button" value="Search" />
							</td>
						</tr>
					</table>
				</div>

			</fieldset>
		</form>

	

						<%
						for(int i=0;i<qList.size();i++){
							Question q = qList.get(i);
							String[] tags = q.getTags();
							
							String title = q.getTitle();
							String[] transTitle = new String[1];
							transTitle[0] = title;
							String[] resultTitle = t.transString(transTitle, originLanguage);
							title = resultTitle[0];
							
							String content = q.getContent();
							String[] transContent = new String[1];
							transContent[0] = content;
							String[] resultContent = t.transString(transContent, originLanguage);
							content = resultContent[0];
						%>
			<table width=600px>
				<tr>
					<td>
						<%=q.getQid()%>
					</td>
					<td>
					</td>
					<td>
						<%=title %>
					</td>
				</tr>
				<tr>
					<td>
						<%=q.getVote()%>
					</td>
					<td>
					</td>
					<td>
						<%=content %>
					</td>
				</tr>
				<tr>
					<td>	
						Votes
					</td>
					<td>
					</td>
					<td>
						<%
							for (String tag : tags) {
						%>
						<%=tag + ";"%>
						<%
							}
						%>
						<br>
					</td>
				</tr>
			</table>
			<hr>
	
	
			<%--<tr>
				<td>
					<%=q.getQid() %>
				</td>
				<td>
					<%=q.getVote() %>
				</td>
				<td>
					<%=q.getTitle() %>
				</td>
				<td>
				<%
   				for(String tag:tags){
   				%>
					<%=tag+";" %>
				<%		
   				}
   				%>
				</td>
			</tr>
		--%><%
			}
		%>
	</div>
	</div>
	<div id="footer">
		<div>
			<div class="connect">
				<h4>Follow us:</h4>
				<ul>
					<li class="facebook"><a href="http://facebook.com/freewebsitetemplates" target="_blank">facebook</a></li>
					<li class="twitter"><a href="http://twitter.com/fwtemplates" target="_blank">twitter</a></li>
				</ul>
			</div>
			<p>Copyright &copy; 2016. All rights reserved.</p>
		</div>
	</div>
	</body>
</html>
