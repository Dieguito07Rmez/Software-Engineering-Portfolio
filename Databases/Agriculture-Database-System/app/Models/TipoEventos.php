<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class TipoEvento extends Model
{
    protected $table = 'tipo_eventos';
    protected $primaryKey = 'id_tipoevento';
    protected $fillable = ['id_tipoevento','nombre_evento'];
    public $timestamps = true;
}
