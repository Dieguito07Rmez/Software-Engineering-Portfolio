<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class eventos extends Model
{
    //La instruccion primaryKey es importante puesto que
    //laravel se va a la tabla departamentes y busca por defaut
    //la llave id, al pornerle la llave primaria de la tabla
    //Le estamos diciendo que cuando hagamos una consulta u operacion
    //vas buscar en la tabla desde la llave primera que le pedimos (idd)
    use HasFactory;
    protected $table = 'eventos_uno';
    protected $primaryKey = 'ideve';
    protected $fillable = ['ideve','nombre','idcat','descripcion','idtaller','fecha','hora','ubicacion','dirigido','encargado1','encargado2','imagen','link'];
    public $timestamps = true;
}
