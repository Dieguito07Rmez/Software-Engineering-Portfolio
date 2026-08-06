<html> 
    <head>
        <title>Login - MOZO'S</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Raleway:wght@400;500;600&family=Open+Sans:wght@400;500&display=swap" rel="stylesheet">
        <style>
            .fuente-principal {
                font-family: "Raleway", sans-serif;
                font-optical-sizing: auto;
                font-weight: 400;
                letter-spacing: 1px;
                text-transform: uppercase;
            }
            .fuente-secundaria {
                font-family: "Open Sans", sans-serif;
                font-optical-sizing: auto;
                font-weight: 400;
                letter-spacing: 1px;
                text-transform: uppercase;
            }
            body { 
                margin: 0; 
                padding: 0; 
                background-color: #6a5d52; 
                min-height: 100vh;
                display: flex;
                flex-direction: column;
            }
            
            /* COLOR PERSONALIZADO DEL NAVBAR */
            .navbar-mozos {
                background-color: #1c1c1b !important;
            }
            
            /* Contenedor de login con color diferente */
            .login-container {
                background-color: #3a3329;
                border-radius: 15px;
                padding: 2.5rem;
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
                max-width: 450px;
                width: 100%;
                margin: 2rem auto;
            }
            
            /* Estilos para los campos del formulario */
            .form-control {
                background-color: #2a241e;
                border: 1px solid #5a4d3f;
                color: #E0E0E0;
                padding: 0.75rem 1rem;
            }
            
            .form-control:focus {
                background-color: #2a241e;
                border-color: #d4af37;
                color: #E0E0E0;
                box-shadow: 0 0 0 0.25rem rgba(212, 175, 55, 0.25);
            }
            
            .form-control::placeholder {
                color: #8a7d6f;
            }
            
            /* Animación para el título */
            .animate-pop {
                animation: popIn 0.6s ease-out;
            }
            
            @keyframes popIn {
                0% {
                    opacity: 0;
                    transform: scale(0.8);
                }
                100% {
                    opacity: 1;
                    transform: scale(1);
                }
            }
            
            /* Estilo para el botón */
            .btn-login {
                background-color: #d4af37;
                border: none;
                color: #1c1c1b;
                font-weight: 600;
                padding: 0.75rem 2rem;
                transition: all 0.3s ease;
                width: 100%;
                margin-top: 1rem;
            }
            
            .btn-login:hover {
                background-color: #b8941f;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            }
            
            /* Estilo para las etiquetas */
            .form-label {
                margin-bottom: 0.5rem;
                display: block;
            }
        </style>
    </head>
    <body>
        @include('layouts.navbar')
        
        <div class="container my-5">
            <div class="login-container">
                <h1 class="fuente-principal text-white text-center mb-4 animate-pop">Login</h1>
                
                <form>
                    <div class="mb-4">
                        <label class="fuente-secundaria form-label" style="color: #E0E0E0; font-size: 1.1rem;">
                            Correo
                        </label>
                        <input type="email" class="form-control" placeholder="Ingresa tu correo">
                    </div>
                    
                    <div class="mb-4">
                        <label class="fuente-secundaria form-label" style="color: #E0E0E0; font-size: 1.1rem;">
                            Contraseña
                        </label>
                        <input type="password" class="form-control" placeholder="Ingresa tu contraseña">
                    </div>
                    <center><a class="btn btn-outline-warning" >Ingresar</a></center>
                </form>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>