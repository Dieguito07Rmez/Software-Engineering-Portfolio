<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;

class PHController extends Controller{

    public function inicio(){
        return view('inicio');
    }

    public function layout(){
        return view('layout');
    }

    public function inventario(){
        return view('inventario');
    }

    
    public function opiniones(){
        return view('opiniones');
    }

    public function ubicacion(){
        return view('ubicacion');
    }
}