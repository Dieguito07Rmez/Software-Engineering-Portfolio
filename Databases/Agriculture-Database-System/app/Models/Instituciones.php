<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Institucion extends Model
{
    protected $table = 'instituciones';
    protected $primaryKey = 'id_institucion';
    protected $fillable = ['id_institucion','nombre','tipo','ubicacion'];
    public $timestamps = true;
}
