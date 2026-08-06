<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>KOPPEE - Coffee Shop HTML Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="Free Website Template" name="keywords">
        <meta content="Free Website Template" name="description">

        <!-- Favicon -->
        <link href="{{ asset('img/favicon.ico')}}" rel="icon">

        <!-- Google Font -->
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200;400&family=Roboto:wght@400;500;700&display=swap" rel="stylesheet"> 

        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="{{ asset('lib/owlcarousel/assets/owl.carousel.min.css')}}" rel="stylesheet">
        <link href="{{ asset('lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css')}}" rel="stylesheet" />
        <!-- Bootstrap CSS -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="{{ asset('css/style.min.css')}}" rel="stylesheet">
        <!--
        <style>
        .nav-bar {
            background: #33211D !important;
            position: relative !important;
        }
        .navbar-brand {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .navbar-brand img {
            width: 90px;
            height: 90px;
            object-fit: contain;
        }
        .nav-bar {
            background-color: #343a40;
        }
        </style>
        <img src="{{ asset('img/Hlogo.png') }}" alt="Hanny">
        -->

    </head>
    <body>
        <!-- Navbar Start -->
        <div class="container-fluid p-0 nav-bar">
            <nav class="navbar navbar-expand-lg bg-none navbar-dark py-3">
                <a href="index.html" class="navbar-brand px-lg-4 m-0">
                    <h1 class="m-0 display-4 text-uppercase text-white">Hanny</h1>
                </a>
                <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse justify-content-between" id="navbarCollapse">
                    <div class="navbar-nav ml-auto p-4">
                        
                        <div class="nav-item dropdown">
                            <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown">Opciones</a>
                            <div class="dropdown-menu text-capitalize">
                                <a href="{{route('opiniones')}}" class="dropdown-item">Opiniones</a>
                                <a href="{{route('inventario')}}" class="dropdown-item">Producto disponible</a>
                                <a href="{{route('ubicacion')}}" class="dropdown-item">Ubicación</a>
                            </div>
                        </div>
                        <a href="{{route('inicio')}}" class="nav-item nav-link active">Inicio</a>
                    </div>
                </div>
            </nav>
        </div>
    @yield('contenido');
            <!-- JavaScript Librerias desde CDN -->
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.bundle.min.js"></script>
            <!-- JavaScript Librerias desde CDN -->
    <script src="{{ asset('lib/easing/easing.min.js')}}"></script>
    <script src="{{ asset('lib/waypoints/waypoints.min.js')}}"></script>
    <script src="{{ asset('lib/owlcarousel/owl.carousel.min.js')}}"></script>
    <script src="{{ asset('lib/tempusdominus/js/moment.min.js')}}"></script>
    <script src="{{ asset('lib/tempusdominus/js/moment-timezone.min.js')}}"></script>
    <script src="{{ asset('lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js')}}"></script>

    <!-- Contact Javascript File -->
    <script src="{{ asset('mail/jqBootstrapValidation.min.js')}}"></script>
    <script src="{{ asset('mail/contact.js')}}"></script>

    <!-- Template Javascript -->
    <script src="{{ asset('js/main.js')}}"></script>
    </body>
</html>
