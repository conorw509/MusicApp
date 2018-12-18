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
  
    </head>
<!------ Include the above in your HEAD tag ---------->

<body id="LoginForm"> 
<div class="container">
<div class="login-form">
<div class="user-main-div">
    
       WELCOME ${email} 
            <div class="form-group">
               <input type="password" class="form-control" id="search" placeholder="Search" required>       
               <button type="submit" class="btn btn-primary btn-search">Search</button>
            </div>

     <a href="http://localhost:8080/MusicApp/webresources/services/LogOut">Log Out</a> 
            
    
  <%
         if(session.getAttribute("email")==null){
             response.sendRedirect("userLogin.html");
         }
         %>
    
      
        
          
         
    
    
</div></div></div>


</body>
</html>
