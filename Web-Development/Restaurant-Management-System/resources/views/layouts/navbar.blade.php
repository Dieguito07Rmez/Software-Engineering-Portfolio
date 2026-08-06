<!DOCTYPE html>
<html>
<head>
    <title>@yield('title', 'Savor Soft')</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Marcellus&family=Viaoda+Libre&display=swap');
    </style>
    <style>
        .fuente-principal {
            font-family: "Marcellus", sans-serif;
            font-optical-sizing: auto;
            font-weight: 400;
            letter-spacing: 1 px;
            text-transform: uppercase;
        }
        .fuente-secundaria {
            font-family: "Viaoda Libre", sans-serif;
            font-optical-sizing: auto;
            font-weight: 400;
            letter-spacing: 1px;
            text-transform: uppercase;
        }
        body { margin: 0; padding: 0; background-color: #706f68ff; }
        .container-fluid { padding: 0; margin: 0; }
        
        /* COLOR PERSONALIZADO DEL NAVBAR */
        .navbar-mozos {
            background-color: #000000ff !important;
            font-family: "Viaoda Libre", sans-serif !important;
        }
        
        /* PARA DROPDOWN */
        .dropdown-menu {
            display: block !important;
            opacity: 0;
            visibility: hidden;
            transform: translateY(-10px);
            transition: all 0.3s ease;
            pointer-events: none;
        }
        
        .dropdown-menu.show {
            opacity: 1;
            visibility: visible;
            transform: translateY(0);
            pointer-events: all;
            display: block !important;
        }
        
        .navbar-nav .dropdown-menu {
            position: absolute !important;
            z-index: 9999 !important;
            background-color: #1c1c1b !important;
        }
        
        .dropdown-item {
            color: white !important;
        }
        
        .dropdown-item:hover {
            background-color: #0c0c0cff !important;
        }

        .main-content {
            min-height: calc(100vh - 76px);
            padding: 20px 0;
        }
    </style>
</head>
<body>
    <!-- NAVBAR CON COLOR PERSONALIZADO -->
    <nav class="navbar navbar-expand-lg navbar-dark navbar-mozos sticky-top">
        <div class="container">
            <a class="navbar-brand" href= "{{route('principal')}}">SAVOR</a>

            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                            Catálogos
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">Insumos</a></li>
                            <li><a class="dropdown-item" href="{{route('registros')}}">Platillos</a></li>
                            <li><a class="dropdown-item" href="#">Clientes</a></li>
                            <li><a class="dropdown-item" href="#">Empleados</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#eventos">Armado menús</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#reservaciones">Reservaciones</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#menu">Compras</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#menu">Cotización</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#menu">Ventas</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#menu">Cálculo de nómina</a>
                    </li>

                
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                            Reportes
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">Reservaciones</a></li>
                            <li><a class="dropdown-item" href="#">Ventas</a></li>
                            <li><a class="dropdown-item" href="#">Entradas y salidas de dinero</a></li>
                            <li><a class="dropdown-item" href="#">Insumos por agotarse</a></li>
                            <li><a class="dropdown-item" href="#">Platillos más vendidos</a></li>
                        </ul>
                    </li>

                </ul>    
            </div>
            <div class="d-flex">
                <a href="{{route('login')}}" class="btn btn-outline-light">Login</a>
            </div>
        </div>
    </nav>

    @yield('contenido')
    
    <!-- Bootstrap 5 JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>