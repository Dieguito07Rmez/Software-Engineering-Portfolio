<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\rescontroller;
use App\Http\Controllers\placontroller;

Route::get('principal',[rescontroller::class, 'principal'])->name('principal');
Route::get('login',[rescontroller::class, 'login'])->name('login');
Route::get('registros', [placontroller ::class, 'registros'])->name('registros');
Route::post('guardar', [placontroller::class, 'guardar'])->name('guardar');