@extends('layouts.navbar')
@section('contenido')
     <center>
        <h1>Registrar platillo</h1>
        
        <form action = "{{route('guardar')}}" method = "POST" enctype = "multipart/form-data">
            {{csrf_field()}}
        <table borde=1>
            <tr>
                <td width=60>Clave</td>
                <td>
                    @if($errors->first('id_platillo'))
                    <p class="text-danger">{{$errors->first('id_platillo')}}</p>
                    @endif
                    <input type="text" class="form-control " name='id_platillo' value="#" readonly></td>
            </tr>
            <tr>
                <td width=100>Nombre</td>
                <td>
                    @if($errors->first('nombre'))
                    <p class="text-warning">{{$errors->first('nombre')}}</p>
                    @endif
                    <input type="text" class="form-control " name='nombre' value="{{old('nombre')}}"></td>
            </tr>
            <tr>
                <td width=100>Precio</td>
                <td>
                    @if($errors->first('precio'))
                    <p class="text-warning">{{$errors->first('precio')}}</p>
                    @endif
                    <input type="text" class="form-control " name='precio' value="{{old('precio')}}"></td>
            </tr>
            <tr>
                <td width=60>Categoria</td>
                <td><select name = "categoria" class = "form-select">
                    <option value ="1">Entrada</option>    
                    <option value ="2">Plato fuerte</option> 
                    <option value ="3">Postres</option>
                    <option value ="4">Bebidas</option> 
                    <option value ="5">Especiales</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td width=100>Detalles</td>
                <td>
                    @if($errors->first('detalle'))
                    <p class="text-warning">{{$errors->first('detalle')}}</p>
                    @endif
                    <input type="text" class="form-control " name='detalle' value="{{old('detalle')}}"></td>
            </tr>
            <tr>
                <td>Imagen</td>
                <td>
                    @if($errors->first('imagen'))
                    <p class="text-warning">{{$errors->first('imagen')}}</p>
                    @endif
                <input type="file" class="form-control" name='imagen' ></td>
            </tr>

            <tr><td colspan=2 align= 'right'>
                <button type="submit" class="btn btn-primary">GUARDAR</button>
            </td></tr>

        </table>
    </form></center>
@stop