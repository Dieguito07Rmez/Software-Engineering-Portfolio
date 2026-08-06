@include('layouts.navbar')
<div class="container-fluid position-relative p-0 m-0">
    <img src="imagenes/descarga.png" class="img-fluid w-100" alt="Restaurante" style="height: 100vh; object-fit: cover;">
    <div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center">
        <div class="container">
            <div class="row">
                <div class="col-lg-6 col-md-8">
                    <h1 class="fuente-principal text-white display-1 animate-pop">Savor Soft</h1>
                    <hr class="w-50 border-3 animate-pop" style="border-color: #dadadaff !important;">
                    <p class="fuente-secundaria text-white fs-3 fw-bold animate-pop">Donde la tradición se encuentra con la innovación</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Sección Eventos -->
<section class="py-5" style="background-color: #565449ff;">
    <div class="container">
        <div class="row">
            <div class="col-12 text-center mb-5">
                <h2 class="fuente-principal display-1" style="color: #D4AF37; text-shadow: 2px 2px 4px rgba(0,0,0,0.5);">Nuestros Eventos</h2>
                <p class="fuente-secundaria" style="color: #E0E0E0; font-size: 1.2rem;">Vive experiencias únicas y memorables</p>
            </div>
        </div>
        
        <div class="row g-4">
            <!-- Card 1 - Boda -->
            <div class="col-md-6 col-lg-4">
                <div class="card h-100 shadow-lg border-0 card-animate fade-in-up" style="background: linear-gradient(145deg, #3E2723, #4E342E);">
                    <div class="card-img-top position-relative" style="height: 200px;">
                        <img src="imagenes/boda.jpeg" 
                             class="w-100 h-100" 
                             alt="Celebración de Bodas" 
                             style="object-fit: cover;">
                        <div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center text-white">
                            <div class="text-center">
                                <h4 class="mb-0" style="color: #FFFFFF; text-shadow: 2px 2px 6px rgba(0,0,0,0.8); font-weight: bold;">Bodas</h4>
                            </div>
                        </div>
                    </div>
                    <div class="card-body d-flex flex-column text-white">
                        <h5 class="card-title fw-bold" style="color: #D4AF37;">Celebración de Bodas</h5>
                        <p class="card-text" style="color: #E0E0E0;">
                            Hacemos de tu día especial una experiencia inolvidable. 
                            Menú personalizado, decoración elegante y atención exclusiva.
                        </p>
                        <div class="mt-auto">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <small style="color: #BCAAA4;">
                                    <i class="bi bi-people"></i> Hasta 150 personas
                                </small>
                                <small style="color: #BCAAA4;">
                                    <i class="bi bi-geo-alt"></i> Salón principal
                                </small>
                            </div>
                            <div class="d-grid">
                                <button class="btn btn-outline-warning">Solicitar cotización</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Card 2 - Cumpleaños -->
            <div class="col-md-6 col-lg-4">
                <div class="card h-100 shadow-lg border-0 card-animate fade-in-up" style="background: linear-gradient(145deg, #3E2723, #4E342E);">
                    <div class="card-img-top position-relative" style="height: 200px;">
                        <img src="imagenes/cumple.jpeg" 
                             class="w-100 h-100" 
                             alt="Fiestas de Cumpleaños" 
                             style="object-fit: cover;">
                        <div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center text-white">
                            <div class="text-center">
                                <h4 class="mb-0" style="color: #FFFFFF; text-shadow: 2px 2px 6px rgba(0,0,0,0.8); font-weight: bold;">Cumpleaños</h4>
                            </div>
                        </div>
                    </div>
                    <div class="card-body d-flex flex-column text-white">
                        <h5 class="card-title fw-bold" style="color: #D4AF37;">Fiestas de Cumpleaños</h5>
                        <p class="card-text" style="color: #E0E0E0;">
                            Celebra tu día especial con nosotros. Ambiente festivo, 
                            menú para todos los gustos y servicio excepcional.
                        </p>
                        <div class="mt-auto">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <small style="color: #BCAAA4;">
                                    <i class="bi bi-people"></i> Hasta 80 personas
                                </small>
                                <small style="color: #BCAAA4;">
                                    <i class="bi bi-geo-alt"></i> Salón familiar
                                </small>
                            </div>
                            <div class="d-grid">
                                <button class="btn btn-outline-warning">Solicitar cotización</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Card 3 - Graduaciones -->
            <div class="col-md-6 col-lg-4">
                <div class="card h-100 shadow-lg border-0 card-animate fade-in-up" style="background: linear-gradient(145deg, #3E2723, #4E342E);">
                    <div class="card-img-top position-relative" style="height: 200px;">
                        <img src="imagenes/graduacion.jpeg" 
                             class="w-100 h-100" 
                             alt="Fiestas de Graduación" 
                             style="object-fit: cover;">
                        <div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center text-white">
                            <div class="text-center">
                                <h4 class="mb-0" style="color: #FFFFFF; text-shadow: 2px 2px 6px rgba(0,0,0,0.8); font-weight: bold;">Graduaciones</h4>
                            </div>
                        </div>
                    </div>
                    <div class="card-body d-flex flex-column text-white">
                        <h5 class="card-title fw-bold" style="color: #D4AF37;">Fiestas de Graduación</h5>
                        <p class="card-text" style="color: #E0E0E0;">
                            Celebra este logro académico con estilo. Espacio ideal 
                            para compartir con familiares y amigos este momento único.
                        </p>
                        <div class="mt-auto">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <small style="color: #BCAAA4;">
                                    <i class="bi bi-people"></i> Hasta 100 personas
                                </small>
                                <small style="color: #BCAAA4;">
                                    <i class="bi bi-geo-alt"></i> Salón eventos
                                </small>
                            </div>
                            <div class="d-grid">
                                <button class="btn btn-outline-warning">Solicitar cotización</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Bootstrap 5 JavaScript -->
    <script src="js/index.js"></script>