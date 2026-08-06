<?php

namespace App\Http\Controllers;

use Illuminate\http\Request;
use App\Models\platillos;
use Session;

class placontroller extends Controller
{
    public function registros()
    {
        return view('catalogos.platillos.registro');
    }

    public function guardar(Request $request)
    {
        // Solo para prueba - mostrar que recibe los datos
        dd($request->all()); // Esto mostrará los datos del formulario
    }
    

}