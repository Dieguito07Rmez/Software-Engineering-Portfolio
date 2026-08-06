@extends('layout')
@section('contenido')

<style>
.btn-azul{
    background:#1976d2;
    color:white;
    padding:5px 10px;
    border-radius:5px;
    text-decoration:none;
    font-size:13px;
    display:inline-block;
}

.btn-azul:hover{
    background:#1565c0;
    color:white;
}

.btn-rojo{
    background:#d32f2f;
    color:white;
    border:none;
    padding:5px 10px;
    border-radius:5px;
    cursor:pointer;
    font-size:13px;
}

.btn-rojo:hover{
    background:#b71c1c;
}
</style>
<br>
<div class="container mt-4 text-center">
    <h2>Reporte de eventos</h2>
    <br>
    <div id="tabla_reporte">
        <div class="d-flex justify-content-center">
            <table style="
                border:2px solid #2f5d50;
                border-collapse: collapse;
                width: 90%;
                background:white;">

                <tr style="background-color:#2f5d50; color:white;">
                    <th style="border:1px solid #2f5d50; padding:8px;">ID</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Encargado</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Institución</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Tipo</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Tema / Empresa</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Capacidad</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Número</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Costo</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Total</th>
                    <th style="border:1px solid #2f5d50; padding:8px;">Opciones</th>
                </tr>

                @foreach($eventos as $e)
                    <tr style="border-top:1px solid #2f5d50;">
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->id_evento}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->encargado}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->institucion}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->nombre_evento}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->campo1}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->capacidad_asistentes}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            {{$e->numero_asistentes}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            ${{$e->costo_asistente}}
                        </td>
                        <td style="padding:8px; border:1px solid #2f5d50;">
                            ${{$e->costo_total}}
                        </td>

                        <td style="padding:8px; border:1px solid #2f5d50;">
                            <div style="display:flex; justify-content:center; gap:8px;">

                            <a href="{{ url('editarevento') }}?id_detalles={{ $e->id_detalles }}" 
                                class="btn-azul">
                                EDITAR
                            </a>

                                <button class="btn-eliminar btn-rojo"
                                        data-id="{{$e->id_detalles}}"
                                        data-evento="{{$e->id_evento}}">
                                    ELIMINAR
                                </button>
                            </div>
                        </td>
                    </tr>
                @endforeach
            </table>
        </div>
    </div>
</div>
<br>
<br>
<script>
$(document).ready(function(){

    $(document).on("click", ".btn-eliminar", function(){

        let id = $(this).data("id");

        if(confirm("¿Seguro que deseas eliminar este evento?")){
            $("#tabla_reporte").load(
                "{{ url('borraevento_reporte') }}?id_detalles=" + id + " #tabla_reporte > *"
            );
        }
    });
});
</script>
@endsection
