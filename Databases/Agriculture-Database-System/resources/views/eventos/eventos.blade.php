@extends('layout')
@section('contenido')

<script>
$(document).ready(function(){

    $("#duracion").keyup(function(){

        let valor = $(this).val();

        if(valor === ""){
            $("#error_duracion").text("");
            return;
        }

        if(!Number.isInteger(Number(valor)) || Number(valor) <= 0){
            $("#error_duracion").text("Solo se permiten números enteros positivos");
        }else{
            $("#error_duracion").text("");
        }

    });

    $(document).on("keyup change", "#formEvento input, #formEvento select", function(){
        validarFormulario();
    });

    function validarFormulario(){

        let completo = true;

        $("#formEvento input, #formEvento select").each(function(){

            if($(this).attr("name") !== "total"){
                if($(this).val() === ""){
                    completo = false;
                }
            }

        });

        if(
            $("#error_lugar").text() !== "" ||
            $("#error_numero").text() !== "" ||
            $("#error_capacidad").text() !== "" ||
            $("#error_duracion").text() !== ""
        ){
            completo = false;
        }

        $(".error-texto").each(function(){
            if($(this).text() !== ""){
                completo = false;
            }
        });

        $("#agregar").prop("disabled", !completo);
    }

    let datosTipos = {
        1: {},
        2: {}
    };

    let tipoActual = $("#tipoevento").val();

    function guardarDatos(){
        let tipo = $("#tipoevento").val();

        datosTipos[tipo] = {};

        $("#campos input").each(function(){
            datosTipos[tipo][$(this).attr("name")] = $(this).val();
        });
    }

    function restaurarDatos(tipo){
        if(datosTipos[tipo]){
            for(let campo in datosTipos[tipo]){
                $("input[name=" + campo + "]").val(datosTipos[tipo][campo]);
            }
        }
    }

    $("#tipoevento").change(function(){

        guardarDatos();

        let tipo = $(this).val();

        $("#campos").load("{{ url('cargacampos') }}?tipo=" + tipo, function(){

            restaurarDatos(tipo);
            validarFormulario();

        });

    });

    $("#lugar").keyup(function(){

        let valor = $(this).val();
        let regex = /^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/;

        if(valor !== "" && !regex.test(valor)){
            $("#error_lugar").text("Solo se permiten letras");
        }else{
            $("#error_lugar").text("");
        }

    });

    $(document).on("keyup", ".campo-texto", function(){

        let valor = $(this).val();
        let regex = /^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/;
        let error = $(this).next(".error-texto");

        if(valor !== "" && !regex.test(valor)){
            error.text("Solo se permiten letras");
        }else{
            error.text("");
        }

    });

    $("#capacidad").keyup(function(){

        let valor = $(this).val();

        if(valor === ""){
            $("#error_capacidad").text("");
            return;
        }

        if(!Number.isInteger(Number(valor)) || Number(valor) <= 0){
            $("#error_capacidad").text("Debe ser un número entero positivo");
        }else{
            $("#error_capacidad").text("");
        }

    });

    $("#numero").keyup(function(){

        let numero = $(this).val();
        let capacidad = $("#capacidad").val();

        if(numero === ""){
            $("#error_numero").text("");
            return;
        }

        if(!Number.isInteger(Number(numero)) || Number(numero) <= 0){
            $("#error_numero").text("Debe ser un número entero positivo");
            return;
        }

        if(capacidad !== "" && Number(numero) > Number(capacidad)){
            $("#error_numero").text("No puede ser mayor a la capacidad y debe ser entero");
        }else{
            $("#error_numero").text("");
        }

    });

    $("#numero, #costo").keyup(function(){

        let numero = parseFloat($("#numero").val()) || 0;
        let costo = parseFloat($("#costo").val()) || 0;

        $("#total").val(numero * costo);

    });

    $("#agregar").click(function(){

        if(
            $("#error_lugar").text() !== "" ||
            $("#error_numero").text() !== "" ||
            $("#error_capacidad").text() !== ""
        ){
            alert("Corrige los errores antes de continuar");
            return;
        }

        let hayErrorCampos = false;

        $(".error-texto").each(function(){
            if($(this).text() !== ""){
                hayErrorCampos = true;
            }
        });

        if(hayErrorCampos){
            alert("Corrige los campos de texto");
            return;
        }

        let datos = $("#formEvento").serialize();

        $.ajax({
            url: "{{ url('cargacarritoevento') }}",
            method: "GET",
            data: datos,

            success: function(respuesta){

                if(respuesta.includes("ERROR")){

                    $("#error_evento").text(respuesta.replace("ERROR: ", ""));
                    $("#contenedor_error_evento").fadeIn();

                    setTimeout(function(){
                        $("#contenedor_error_evento").fadeOut();
                    }, 3000);

                }else{

                    $("#contenedor_error_evento").fadeOut();
                    $("#error_evento").text("");
                    $("#carrito").html(respuesta);

                }

            },

            error: function(xhr){
                console.log(xhr.responseText);
                alert("Error del servidor, revisa consola");
            }
        });

    });

});
</script>

<style>
@keyframes aparecer {
    from {
        opacity: 0;
        transform: translate(-50%, -60%);
    }
    to {
        opacity: 1;
        transform: translate(-50%, -50%);
    }
}
</style>
<br>
<div class="container mt-4 text-center">

    <h2>Alta Evento</h2>
    <br>
    <form id="formEvento">
        <div class="d-flex justify-content-center">
            <table style="
                border:2px solid #2f5d50;
                border-collapse: collapse;
                padding:10px;
                width: 500px;
                background:white;">
                <tr>
                    <td style="text-align:left; width:200px; padding:8px;">
                        <b>Clave del evento:</b>
                    </td>
                    <td style="padding:8px;">
                        <input type="text" name="id_evento" value="{{$sigue}}" style="width:100%;" readonly>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Fecha:</b></td>
                    <td style="padding:8px;">
                        <input type="date" name="fecha" style="width:100%;">
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Encargado:</b></td>
                    <td style="padding:8px;">
                        <select name="id_encargado" style="width:100%;">
                            @foreach($encargados as $e)
                                <option value="{{$e->id_encargado}}">{{$e->nombre}}</option>
                            @endforeach
                        </select>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Institución:</b></td>
                    <td style="padding:8px;">
                        <select name="id_institucion" style="width:100%;">
                            @foreach($instituciones as $i)
                                <option value="{{$i->id_institucion}}">{{$i->nombre}}</option>
                            @endforeach
                        </select>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Lugar:</b></td>
                    <td style="padding:8px;">
                        <input type="text" name="lugar" id="lugar" style="width:100%;">
                        <small id="error_lugar" style="color:red;"></small>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Tipo de evento:</b></td>
                    <td style="padding:8px;">
                        <select name="id_tipoevento" id="tipoevento" style="width:100%;">
                            @foreach($tipos as $t)
                                <option value="{{$t->id_tipoevento}}">{{$t->nombre_evento}}</option>
                            @endforeach
                        </select>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" style="padding:8px;">
                        <div id="campos"></div>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Duración/Horas:</b></td>
                    <td style="padding:8px;">
                        <input type="text" id="duracion" name="duracion" style="width:100%;">
                        <small id="error_duracion" style="color:red;"></small>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Capacidad:</b></td>
                    <td style="padding:8px;">
                        <input type="number" id="capacidad" name="capacidad" style="width:100%;" min="1" step="1">
                        <small id="error_capacidad" style="color:red;"></small>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Número de asistentes:</b></td>
                    <td style="padding:8px;">
                        <input type="number" id="numero" name="numero" style="width:100%;" min="1" step="1">
                        <small id="error_numero" style="color:red;"></small>
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Costo por asistente:</b></td>
                    <td style="padding:8px;">
                        <input type="text" id="costo" name="costo" style="width:100%;">
                    </td>
                </tr>

                <tr>
                    <td style="text-align:left; padding:8px;"><b>Total:</b></td>
                    <td style="padding:8px;">
                        <input type="text" id="total" name="total" readonly style="width:100%;">
                    </td>
                </tr>

                <tr>
                    <td colspan="2" style="text-align:center; padding:10px;">
                        <button type="button" id="agregar" disabled style="
                            background:#2f5d50;
                            color:white;
                            padding:8px 20px;
                            border:none;
                            border-radius:5px;">
                            Agregar evento
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </form>

    <hr>

    <div id="contenedor_error_evento" style="
        display:none;
        position:fixed;
        top:50%;
        left:50%;
        transform:translate(-50%, -50%);
        z-index:9999;">

        <div id="error_evento" style="
            background:#fdecea;
            color:#b71c1c;
            border:1px solid #f5c6cb;
            padding:18px 30px;
            border-radius:12px;
            width:420px;
            text-align:center;
            font-weight:bold;
            font-size:16px;
            box-shadow:0 8px 20px rgba(0,0,0,0.2);
            animation: aparecer 0.3s ease;
        ">
        </div>

    </div>

    <hr>
    <div id="carrito"></div>
</div>
@endsection
