<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AController;
use App\Http\Controllers\EventosController;


Route::get('layout',[AController::class,'layout'])->name('layout');
Route::get('eventos_uno',[AController::class,'eventos_uno'])->name('eventos_uno');
Route::post('guardaevento',[AController::class,'guardaevento'])->name('guardaevento');
Route::get('reporteventos',[AController::class,'reporteventos'])->name('reporteventos');

//Esta ruta va recibir como parametro el ideve que es propio del evento
Route::get('eliminar/{ideve}',[AController::class,'eliminar'])->name('eliminar');


Route::get('editaevento',[AController::class,'editaevento'])->name('editaevento');
Route::post('cambioevento',[AController::class,'cambioevento'])->name('cambioevento');

Route::get('eventos',[EventosController::class,'eventos'])->name('eventos');
Route::get('cargacampos',[EventosController::class,'cargacampos']);
Route::get('cargacarritoevento',[EventosController::class,'cargacarritoevento']);
Route::get('borraevento',[EventosController::class,'borraevento']);
Route::get('reporteeventos',[EventosController::class,'reporteeventos'])->name('reporteeventos');
Route::get('borraevento_reporte', [EventosController::class, 'borraevento_reporte']);
Route::get('editarevento',[EventosController::class,'editarevento'])->name('editarevento');

