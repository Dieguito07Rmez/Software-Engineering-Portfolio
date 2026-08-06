<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Evento;
use App\Models\DetalleEvento;

class EventosController extends Controller
{
    public function eventos()
    {
        $eventos = \DB::select("SELECT id_evento FROM eventos ORDER BY id_evento DESC LIMIT 1");

        $sigue = count($eventos) == 0 ? 1 : $eventos[0]->id_evento + 1;

        $encargados = \DB::select("SELECT * FROM encargados_eventos");
        $instituciones = \DB::select("SELECT * FROM instituciones");
        $tipos = \DB::select("SELECT * FROM tipo_eventos");

        return view('eventos.eventos')
            ->with('sigue', $sigue)
            ->with('encargados', $encargados)
            ->with('instituciones', $instituciones)
            ->with('tipos', $tipos);
    }

    public function cargacampos(Request $request)
    {
        if ($request->tipo == 1) {
            return view('eventos.campos_capacitacion');
        } else {
            return view('eventos.campos_demo');
        }
    }

    public function cargacarritoevento(Request $request)
    {
        $request->validate([
            'id_evento' => 'required|integer',
            'fecha' => 'required|date',
            'id_encargado' => 'required|integer',
            'id_institucion' => 'required|integer',
            'lugar' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
            'duracion' => 'required|integer|min:1|max:100',
            'capacidad' => 'required|integer|min:1|max:1000',
            'numero' => 'required|integer|min:1|max:1000',
            'costo' => ['required', 'regex:/^[0-9]+(\.[0-9]{1,2})?$/'],
            'total' => ['required', 'regex:/^[0-9]+(\.[0-9]{1,2})?$/'],
        ]);

        if ($request->numero > $request->capacidad) {
            return response("ERROR: Número mayor que capacidad");
        }

        if ($request->id_tipoevento == 1) {
            $request->validate([
                'tema' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
                'cultivo' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
                'material' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
            ]);
        } else {
            $request->validate([
                'empresa' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
                'tecnologia' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
                'maquinaria' => ['required', 'regex:/^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$/'],
            ]);
        }

        $existe = \DB::select(
            "SELECT COUNT(*) as cuantos FROM eventos WHERE id_evento = ?",
            [$request->id_evento]
        );

        if ($existe[0]->cuantos == 0) {
            $evento = new Evento;
            $evento->id_evento = $request->id_evento;
            $evento->fecha = $request->fecha;
            $evento->id_encargado = $request->id_encargado;
            $evento->id_institucion = $request->id_institucion;
            $evento->lugar = $request->lugar;
            $evento->save();
        } 

        $campo = $request->id_tipoevento == 1 ? $request->tema : $request->empresa;

        $existeDetalle = \DB::select(
            "
            SELECT COUNT(*) as cuantos 
            FROM detalle_eventos 
            WHERE id_evento = ? AND campo1 = ? AND id_detalles != ?
            ",
            [
                $request->id_evento,
                $campo,
                $request->id_detalles ?? 0
            ]
        );

        if ($existeDetalle[0]->cuantos > 0) {
            return response("ERROR: Ya existe un evento con ese dato");
        }

        if ($request->id_detalles) {
            $detalle = DetalleEvento::find($request->id_detalles);
        } else {
            $detalle = new DetalleEvento;
        }

        $detalle->id_evento = $request->id_evento;
        $detalle->id_tipoevento = $request->id_tipoevento;

        if ($request->id_tipoevento == 1) {
            $detalle->campo1 = $request->tema;
            $detalle->campo2 = $request->cultivo;
            $detalle->campo3 = $request->material;
        } else {
            $detalle->campo1 = $request->empresa;
            $detalle->campo2 = $request->tecnologia;
            $detalle->campo3 = $request->maquinaria;
        }

        $detalle->duracion = $request->duracion;
        $detalle->capacidad_asistentes = $request->capacidad;
        $detalle->numero_asistentes = $request->numero;
        $detalle->costo_asistente = $request->costo;
        $detalle->costo_total = $request->total;
        $detalle->save();

        $carrito = \DB::select(
            "
            SELECT e.id_evento, en.nombre as encargado, i.nombre as institucion,
            e.lugar, t.nombre_evento, d.*
            FROM detalle_eventos d
            INNER JOIN eventos e ON e.id_evento = d.id_evento
            INNER JOIN encargados_eventos en ON en.id_encargado = e.id_encargado
            INNER JOIN instituciones i ON i.id_institucion = e.id_institucion
            INNER JOIN tipo_eventos t ON t.id_tipoevento = d.id_tipoevento
            WHERE e.id_evento = ?
            ",
            [$request->id_evento]
        );

        return view('eventos.carrito')->with('carrito', $carrito);
    }

    public function borraevento(Request $request)
    {
        \DB::delete(
            "DELETE FROM detalle_eventos WHERE id_detalles = ?",
            [$request->id_detalles]
        );

        $carrito = \DB::select(
            "
            SELECT e.id_evento, en.nombre as encargado, i.nombre as institucion,
            e.lugar, t.nombre_evento, d.*
            FROM detalle_eventos d
            INNER JOIN eventos e ON e.id_evento = d.id_evento
            INNER JOIN encargados_eventos en ON en.id_encargado = e.id_encargado
            INNER JOIN instituciones i ON i.id_institucion = e.id_institucion
            INNER JOIN tipo_eventos t ON t.id_tipoevento = d.id_tipoevento
            WHERE e.id_evento = ?
            ",
            [$request->id_evento]
        );

        return view('eventos.carrito')->with('carrito', $carrito);
    }

    public function reporteeventos()
    {
        $eventos = \DB::select(
            "
            SELECT e.id_evento, e.fecha, en.nombre as encargado, i.nombre as institucion,
            t.nombre_evento, d.campo1, d.campo2, d.campo3, d.id_detalles,
            d.capacidad_asistentes, d.numero_asistentes, d.costo_asistente, d.costo_total
            FROM eventos e
            INNER JOIN detalle_eventos d ON d.id_evento = e.id_evento
            INNER JOIN encargados_eventos en ON en.id_encargado = e.id_encargado
            INNER JOIN instituciones i ON i.id_institucion = e.id_institucion
            INNER JOIN tipo_eventos t ON t.id_tipoevento = d.id_tipoevento
            "
        );

        return view('eventos.reporte')->with('eventos', $eventos);
    }

    public function borraevento_reporte(Request $request)
    {
        \DB::delete(
            "DELETE FROM detalle_eventos WHERE id_detalles = ?",
            [$request->id_detalles]
        );

        $eventos = \DB::select(
            "
            SELECT e.id_evento, e.fecha, en.nombre as encargado, i.nombre as institucion,
            t.nombre_evento, d.id_detalles,
            d.capacidad_asistentes, d.numero_asistentes, d.costo_asistente, d.costo_total
            FROM eventos e
            INNER JOIN detalle_eventos d ON d.id_evento = e.id_evento
            INNER JOIN encargados_eventos en ON en.id_encargado = e.id_encargado
            INNER JOIN instituciones i ON i.id_institucion = e.id_institucion
            INNER JOIN tipo_eventos t ON t.id_tipoevento = d.id_tipoevento
            "
        );

        if ($request->ajax()) {
            return view('eventos.reporte')
                ->with('eventos', $eventos)
                ->renderSections()['contenido'];
        }

        return view('eventos.reporte')->with('eventos', $eventos);
    }

    public function editarevento(Request $request){
    $detalle = \DB::select(
        "SELECT * FROM detalle_eventos WHERE id_detalles = ?",
        [$request->id_detalles]
    );

    $evento = \DB::select(
        "SELECT * FROM eventos WHERE id_evento = ?",
        [$detalle[0]->id_evento]
    );

    $carrito = \DB::select(
        "
        SELECT e.id_evento, en.nombre as encargado, i.nombre as institucion,
        e.lugar, t.nombre_evento, d.*
        FROM detalle_eventos d
        INNER JOIN eventos e ON e.id_evento = d.id_evento
        INNER JOIN encargados_eventos en ON en.id_encargado = e.id_encargado
        INNER JOIN instituciones i ON i.id_institucion = e.id_institucion
        INNER JOIN tipo_eventos t ON t.id_tipoevento = d.id_tipoevento
        WHERE e.id_evento = ?
        ",
        [$detalle[0]->id_evento]
    );

    $encargados = \DB::select("SELECT * FROM encargados_eventos");
    $instituciones = \DB::select("SELECT * FROM instituciones");
    $tipos = \DB::select("SELECT * FROM tipo_eventos");

    return view('eventos.editarevento')
        ->with('evento', $evento[0])
        ->with('carrito', $carrito)
        ->with('encargados', $encargados)
        ->with('instituciones', $instituciones)
        ->with('tipos', $tipos);
    }
}