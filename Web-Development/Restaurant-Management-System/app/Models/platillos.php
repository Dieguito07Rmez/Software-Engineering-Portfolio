<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class platillos extends Model
{
    use HasFactory;
    protected $primaryKey = 'id_platillo';
    protected $fillable=['id_platillo','nombre','precio','categoria','imagen','detalle'];
}
