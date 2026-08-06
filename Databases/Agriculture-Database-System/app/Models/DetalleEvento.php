<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class DetalleEvento extends Model
{
    use HasFactory;
    protected $table = 'detalle_eventos';
    protected $primaryKey = 'id_detalles';
    protected $fillable = ['id_evento','id_tipoevento','campo1','campo2','campo3','duracion','capacidad_asistentes','numero_asistentes','costo_asistente','costo_total'];
}
