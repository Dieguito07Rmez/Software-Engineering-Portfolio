<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\categorias;
use App\Models\eventos;
use App\Models\personales;
use App\Models\talleres;
use Session;

class AController extends Controller{
    public function layout(){
        return view('layout');
    }
    public function eventos_uno(){
        $categorias = categorias::orderBy('nombre')->get();
        return view('eventos_uno')
        ->with('categorias',$categorias);
    }
    
    public function guardaevento(request $request){
        $request->validate([
            'nombre'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'ubicacion'=>'required|regex:/^[A-Z,a-z,0-9\s\.,#\-]+$/', 
            'dirigido'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'encargado1'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'encargado2'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'imagen'=>'mimes:jpg,png,jpeg',
            'descripcion'=>'required|regex:/^[A-Z][A-Z,a-z,0-9, ]+$/' 
        ]);

        

        $file = $request->file('imagen');
		    if ($file != ''){
		        $img = $request->descripcion . $file->getClientOriginalName(); 
		        \Storage::disk('local')->put($img, \File::get($file));
		    }else{
                $img = 'sinfoto.png';
            }

        $eventos = new eventos;
        $eventos->nombre =$request->nombre;
        $eventos->idcat =$request->idcat;
        $eventos->descripcion =$request->descripcion;
        $eventos->fecha =$request->fecha;
        $eventos->hora =$request->hora;
        $eventos->ubicacion =$request->ubicacion;
        $eventos->dirigido =$request->dirigido;
        $eventos->encargado1 =$request->encargado1;
        $eventos->encargado2 =$request->encargado2;
        $eventos->imagen =$img;
        $eventos->link =$request->link;
        $eventos->save();
        
        Session::flash('mensaje', "El evento ha sido dado de alta");
        return redirect()->route('reporteventos');
    }

    public function reporteventos(){
        $eventos = \DB::select("SELECT e.ideve ,e.nombre as nom, c.idcat as cat,
                                c.nombre as nomcat,
                                e.descripcion as dcion, e.fecha, e.hora,
                                e.ubicacion as ubi, e.dirigido as diri, 
                                e.encargado1 as e1, e.encargado2 as e2, e.imagen as img, e.link
        FROM eventos AS e
        INNER JOIN categorias AS c ON c.idcat=e.idcat
        ORDER BY e.nombre ASC");
        return view('reporteventos')
        ->with('eventos',$eventos);
    }

    public function eliminar(request $request){
        $evento  =\DB::delete("delete from eventos where ideve=$request->ideve");
        Session::flash('mensaje',"El evento ha sido eliminado");
        return redirect()->route('reporteventos');
    }

    public function editaevento(request $request){
        $eventos = \DB::select("SELECT e.ideve as ideve ,e.nombre as nombre, c.idcat,
                                c.nombre as cat,
                                e.descripcion as descripcion, e.fecha as fecha,
                                e.hora as hora, e.ubicacion as ubicacion, e.dirigido as dirigido, 
                                e.encargado1 as encargado1, e.encargado2 as encargado2,
                                e.imagen as imagen, e.link as link
        FROM eventos AS e
        INNER JOIN categorias AS c ON c.idcat=e.idcat
        WHERE ideve = $request->ideve");

        $categorias = categorias::all();
        return view('editaevento')
        ->with('eventos',$eventos[0])
        ->with('categorias',$categorias);
    }

    public function cambioevento(request $request){
        $request->validate([
            'nombre'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'ubicacion'=>'required|regex:/^[A-Z,a-z,0-9\s\.,#\-]+$/', 
            'dirigido'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'encargado1'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'encargado2'=>'required|regex:/^[A-Z][A-Z,a-z, ]+$/', 
            'imagen'=>'mimes:jpg,png,jpeg',
            'descripcion'=>'required|regex:/^[A-Z][A-Z,a-z,0-9, ]+$/' 
        ]);

        $file = $request->file('imagen'); 
		    if ($file != ''){
		        $img = $request->ideve . $file->getClientOriginalName();
		        \Storage::disk('local')->put($img, \File::get($file));
		    }

        $eventos = eventos::find($request->ideve);
        $eventos->nombre =$request->nombre;
        $eventos->idcat =$request->idcat;
        $eventos->descripcion =$request->descripcion;
        $eventos->fecha =$request->fecha;
        $eventos->hora =$request->hora;
        $eventos->ubicacion =$request->ubicacion;
        $eventos->dirigido =$request->dirigido;
        $eventos->encargado1 =$request->encargado1;
        $eventos->encargado2 =$request->encargado2;
        if($file != ''){
            $eventos->imagen = $img;
        }
        $eventos->link =$request->link;
        $eventos->save();
        
        Session::flash('mensaje', "El evento ha sido actualizado");
        return redirect()->route('reporteventos');
    }
}