<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\PHController;

Route::get('layout',[PHController::class,'layout'])->name('layout');
Route::get('inventario',[PHController::class,'inventario'])->name('inventario');
Route::get('opiniones',[PHController::class,'opiniones'])->name('opiniones');
Route::get('inicio',[PHController::class,'inicio'])->name('inicio');
Route::get('ubicacion',[PHController::class,'ubicacion'])->name('ubicacion');

