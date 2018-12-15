<%-- 
    Document   : adminpanel
    Created on : 15-Dec-2018, 21:43:06
    Author     : conor
--%>


<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<!------ Include the above in your HEAD tag ---------->


<html>
  <head>
   <link href="style.css" rel="stylesheet" type="text/css"/>
   <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
   <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
   <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!------ Include the above in your HEAD tag ---------->
  </head>
<body id="LoginForm">
<div class="container">

   
     <form  method ="post">
    <div class="main-div">
    <div class="panel">
   <h2>Admin Panel!</h2>
   WELCOME ${email}
   <p>Please fill in the following details to add.</p>
   </div>
  <div class="form-group">
        <input type="text" class="form-control" name="id" placeholder="ID"> 
        </div>
         <div class="form-group">
        <input type="text" class="form-control" name="title" placeholder="Title">
        </div>
        <div class="form-group">
        <input type="text" class="form-control" name="artist" placeholder="Artist">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" name="album" placeholder="Album">
            <br>
            <input type="text" class="form-control" placeholder="Genre" name="genre">
        </div>
                 <input type ="submit" value="Add" formaction="http://localhost:8080/MusicApp/webresources/adminServices/add">
                  <input type ="submit" value="Delete" formaction="http://localhost:8080/MusicApp/webresources/adminServices/delete">
       
      <!--  <button type="submit" class="btn btn-primary" onclick="return Validation()">Login</button>-->
      <a href="index.html">Log Out</a>
   
        
    </form>
     <%
         if(session.getAttribute("email")==null){
             response.sendRedirect("adminLogin.html");
         }
         %>
</div>

</body>
</html>
