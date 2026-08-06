<style>
.tabla-estilo {
    border-collapse: separate !important;
    border-spacing: 0 12px;
}

.tabla-estilo tbody tr {
    background: #fff;
    border: 2px solid #2f5d50;
}

table thead th {
    background-color: #2f5d50 !important;
    color: white !important;
    border: 2px solid #1e3d35 !important;
    text-align: center;
    vertical-align: middle;
}

.tabla-contenedor {
    display: flex;
    justify-content: center;
}

.tabla-estilo {
    width: 90%;
    border: 2px solid #2f5d50;
    background: white;
}

.tabla-estilo td {
    vertical-align: middle;
}

.tabla-interna td {
    border: 1px solid #bbb;
    padding: 6px;
    text-align: left;
}

.btn-danger {
    background-color: #d9534f;
    border: none;
}
</style>

<div class="tabla-contenedor mt-4">
    <table class="table table-bordered text-center tabla-estilo">

        <thead>
            <tr>
                <th>Encargado del evento</th>
                <th>Institución organizadora</th>
                <th>Lugar</th>
                <th>Tipo de evento</th>
                <th>Detalles del evento</th>
                <th>Capacidad de asistentes</th>
                <th>Número de asistentes</th>
                <th>Costo por asistente</th>
                <th>Costo total</th>
                <th>Acción</th>
            </tr>
        </thead>

        <tbody>
        @foreach($carrito as $c)
        <tr>
            <td>{{$c->encargado}}</td>
            <td>{{$c->institucion}}</td>
            <td>{{$c->lugar}}</td>
            <td>{{$c->nombre_evento}}</td>

            <td style="padding:0;">
                <table class="tabla-interna" style="width:100%; border-collapse: collapse;">
                    
                    @if($c->id_tipoevento == 1)
                        <tr>
                            <td><b>Tema de capacitación</b></td>
                            <td>{{$c->campo1}}</td>
                        </tr>
                        <tr>
                            <td><b>Cultivo dirigido</b></td>
                            <td>{{$c->campo2}}</td>
                        </tr>
                        <tr>
                            <td><b>Material entregado</b></td>
                            <td>{{$c->campo3}}</td>
                        </tr>
                    @else
                        <tr>
                            <td><b>Empresa o proveedor</b></td>
                            <td>{{$c->campo1}}</td>
                        </tr>
                        <tr>
                            <td><b>Tecnología presentada</b></td>
                            <td>{{$c->campo2}}</td>
                        </tr>
                        <tr>
                            <td><b>Tipo de maquinaria</b></td>
                            <td>{{$c->campo3}}</td>
                        </tr>
                    @endif

                </table>
            </td>

            <td>{{$c->capacidad_asistentes}}</td>
            <td>{{$c->numero_asistentes}}</td>
            <td>${{$c->costo_asistente}}</td>
            <td><b>${{$c->costo_total}}</b></td>

            <td>
                <button class="btn btn-danger borrar" data-id="{{$c->id_detalles}}">
                    ELIMINAR
                </button>
            </td>
        </tr>
        @endforeach
        </tbody>
    </table>
</div>

<script>
$(".borrar").click(function(){
    let id = $(this).data('id');
    let id_evento = $("input[name=id_evento]").val();
    $("#carrito").load("{{ url('borraevento') }}?id_detalles="+id+"&id_evento="+id_evento);
});
</script>