<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class EncargadoEvento extends Model
{
    protected $table = 'encargados_eventos';
    protected $primaryKey = 'id_encargado';
    protected $fillable = ['id_encargado','nombre','correo','telefono'];
    public $timestamps = true;
}
