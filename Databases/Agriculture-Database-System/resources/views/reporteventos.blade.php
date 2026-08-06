@extends('layout')
@section('contenido')
<br><br>

<div class="container" style="max-width: 1200px;">
    
    {{-- Botón alineado a la izquierda --}}
    <div class="text-start mb-3">
        <a href="{{route('eventos')}}">
            <button type="button" class="btn btn-success">Alta de evento</button>
        </a>
    </div>

    @if (Session::has('mensaje'))
    <div>
        <div class="alert alert-dismissible alert-success">
            <button type="button" class="btn-close" data-bsdismiss="alert"></button>
            {{Session::get('mensaje')}}
        </div>
    </div>
    @endif

    <table class="table table-hover">
        <thead>
            <tr class="table-dark">
                <th scope="col">Clave</th>
                <th scope="col">Nombre del Evento</th>
                <th scope="col">Categoria</th>
                <th scope="col">Fecha</th>
                <th scope="col">Hora</th>
                <th scope="col">Ubicación</th>
                <th scope="col">Modificaciones</th>
            </tr>
        </thead>
        <tbody>
            @foreach($eventos as $e)
            <tr>
                <th scope="row">{{$e->ideve}}</th>
                <td>{{$e->nom}}</td>
                <td>{{$e->nomcat}}</td>
                <td>{{$e->fecha}}</td>
                <td>{{$e->hora}}</td>
                <td>{{$e->ubi}}</td>
                <td>
                    <a href="{{route('editaevento',['ideve'=>$e->ideve])}}">
                        <button type="button" class="btn btn-outline-primary">Modificar</button>
                    </a>
                    <a href="{{route('eliminar',['ideve'=>$e->ideve])}}">
                        <button type="button" class="btn btn-outline-primary">Eliminar</button>
                    </a>
                </td>
            </tr>
            @endforeach
        </tbody>
    </table>

</div>
@stop
