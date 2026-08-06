@extends('layout')
@section('contenido')
<br>
<br>
<center><h1>Edita evento</h1></center>
<center>
<br>
<br>
    <form action="{{route('cambioevento')}}" method="POST" enctype = "multipart/form-data">
    {{csrf_field()}}
        <input type="hidden" name="ideve" value="{{ $eventos->ideve }}">
        
        <table border=1>
            <tr>
                <td width=100>Nombre:</td>
                <td>
                @if($errors->first('nombre'))
                <p class="text-danger">{{$errors->first('nombre')}}</p>
                @endif
                <input type="text" class="form-control" name="nombre" id="nombre" value="{{$eventos->nombre}}">
                </td>
            </tr>
            <tr>
                <td width=100>Tipo:</td>
                <td>
                    <select name="idcat" class="form-select">
                        <!--Instruccion de Blade que recorre un array de una variable
                            ,como categorias-->
                        <option value='{{$eventos->idcat}}'>{{$eventos->cat}}</option>
                        @foreach($categorias as $cat)
                        <option value="{{$cat->idcat}}">{{$cat->nombre}}</option>
                        @endforeach
                    </select>
                </td>
            </tr>
            <tr>
                <td width=100>Fecha:</td>
                <td>
                    <input type="date" class="form-control" name="fecha" id="fecha" value="{{$eventos->fecha}}">
                </td>
            </tr>
            <tr>
                <td width=100>Hora:</td>
                <td>
                <input type="time" class="form-control" name="hora" id="hora" value="{{$eventos->hora}}">
                </td>
            </tr>
            <tr>
                <td width=100>Ubicación:</td>
                <td>
                    @if($errors->first('ubicacion'))
                    <p class="text-danger">{{$errors->first('ubicacion')}}</p>
                    @endif
                    <input type="text" class="form-control" name="ubicacion" id="ubicacion" value="{{$eventos->ubicacion}}">
                </td>
            </tr>
            <tr>
                <td width=100>Dirigido para:</td>
                <td>
                    @if($errors->first('dirigido'))
                    <p class="text-danger">{{$errors->first('dirigido')}}</p>
                    @endif
                    <input type="text" class="form-control" name="dirigido" id="dirigido" value="{{$eventos->dirigido}}">
                </td>
            </tr>
            <tr>
                <td width=100>Encargado 1:</td>
                <td>
                    @if($errors->first('encargado1'))
                    <p class="text-danger">{{$errors->first('encargado1')}}</p>
                    @endif
                    <input type="text" class="form-control" name="encargado1" id="encargado1" value="{{$eventos->encargado1}}">
                </td>
            </tr>
            <tr>
                <td width=100>Encargado 2:</td>
                <td>
                    @if($errors->first('encargado2'))
                    <p class="text-danger">{{$errors->first('encargado2')}}</p>
                    @endif
                    <input type="text" class="form-control" name="encargado2" id="encargado2" value="{{$eventos->encargado2}}">
                </td>
            </tr>
            <tr>
                <td width=100>Imagen:</td>
                <td>
                    @if($errors->first('imagen'))
                    <p class="text-danger">{{$errors->first('imagen')}}</p>
                    @endif
                    <input type="file" class="form-control" name="imagen" id="imagen" value="{{$eventos->imagen}}">
                </td>
            </tr>
            <tr>
                <td width=100>Link:</td>
                <td>
                    <input type="url" class="form-control" name="link" id="link" value="{{$eventos->link}}">
                </td>
            </tr>
            <tr>
                <td width=100>Descripción:</td>
                <td>
                    @if($errors->first('descripcion'))
                    <p class="text-danger">{{$errors->first('descripcion')}}</p>
                    @endif
                    <input type="text" class="form-control" name="descripcion" value="{{$eventos->descripcion}}">
                </td>
            </tr>
            <tr>
                <td colspan=2 align="right">
                <button type="submit" class="btn btn-dark">Guardar</button>
                </td>
            </tr>
        </table>
    </form>
</center>
<br>
<br>
@stop