<?php

namespace App\Http\Controllers;

use Illuminate\http\Request;

class rescontroller extends Controller
{
    public function principal()
    {
        return view('index');
    }
    
    public function navbar()
    {
        return view('layouts/navbar');
    }
    
    public function login()
    {
        return view('auth/login');
    }
}