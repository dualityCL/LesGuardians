# LesGuardians
![Version](https://img.shields.io/badge/Version-2.7.5_R2-purple.svg) ![Rev](https://img.shields.io/badge/Rev-R2-purple.svg) ![Build Status](https://img.shields.io/badge/build-ENDED-purple.svg)

<p align="center"><img src="https://i.imgur.com/SubpjnI.png" width="50%"><br>Escrito en Java</p>

# Introducción (Motivo del proyecto)
Inicio este proyecto porque los desarrolladores han dejado de trabajar en él a finales del año 2012 y a modo de aprendizaje decidí decompilar la última versión publicada por ellos, la v2.7.5 (compilada el 28 de novimebre de 2012).

# Changelog 

🌀 v2.7.5-R2 (03/03/2020 - 05/03/2020) - VERSIÓN ESTABLE
* Puedes descargar el emulador completo reorganizado (jar, src, db, clips, lang) haciendo [clic aquí](https://github.com/RSPAWN/LesGuardians/releases/tag/v2.7.5-R2), donde también encontrarás el changelog completo de dicha versión.

🌀 v2.7.5-R1 - Versión base (02/03/2020)
* Esta versión es el resultado de la decompilación del JAR, por lo tanto los errores están a la orden del día en el código fuente.
* El servidor ni siquiera puede iniciarse correctamente, durante cierto punto ya era posible pero el Pathfinding estaba totalmente estropeado al igual que varios objetos interactivos.
* Comandos: Corrección aplicada a los comandos "who", "SHOWFIGHTPOS", "TELEPORT", "IRMAPA", "ENERGIA", "ITEM", "KAMAS", "ENCHERVIDA" y "LEVEL".
* Constantes: Corrección aplicada a los métodos "getObjetoPorTrabajo" y "esRetoPosible1".
* IA: Reconstrucción de los métodos "listaEnemigosMenosPDV", "listaTodoLuchadores" y "objetivosMasCercanos".
* SQLManager: Reconstrucción de los métodos "analizarHechizoStats" y "ejecutarConsultaSQL".
* GameThread: Correcciones menores.
* Acción: Corregido constructor Prisma en "case 201".
* Maps: Corrección aplicada a los métodos "delAccionFinPelea", "getObjectosGDF", "refrescarGrupoMobs" y "copiarMapa".
* ConsolePersonalizado: Reparado void run().
* Conta: Reconstrucción del método "resetTodosPjs".
* Pathfinding: Reconstrucción de los métodos "numeroMovimientos", "checkearLineaDeVista", "listaCeldasDesdeLuchador" y "getDirEntreDosCeldas".

A continuación se encuentran listados todos los cambios realizados por los autores originales del proyecto:

🌀 Versión 2.7.5 (~2012)
* Bonificaciones de conjuntos (sets) de clase (60%).
* Comandos para Desbug (iniciarluta y passarturno con condiciones anti-bug).
* Cercados (cruzar monturas con objetos de cría).
* Prismas.
* Reconexión en combate.
* Reinicio automático de servidor, configurable.
* Sistema de soporte (50%).
* Lang.
* Estabilidad mejorada.
* IA trabajada (95%).
* Todas las acciones de objetos desbugeadas.
* Tienda in-game.
* Vídeo tutorial.
* Territorios conquistables.
* Modo heroíco.
* Comandos nuevos en la consola.
* Consola (CMD) personalizada.
* Items 2.0/2.8 (Actualizaciones constantes).

🌀 v2.5.0

🌀 v2.3.4
* Se corrigió la creación de personajes.
* Corrección de problemas SQL.
* Se eliminaron los prefijos en la base de datos (ahora es posible usar cualquier CMS).

🌀 v2.3.3

🌀 v2.3.0

🌀 v2.2.0

🌀 v2.0.0

🌀 v1.5.0

🌀 v1.0.5

🌀 v1.0.0
* Casamiento.
* Dragopavo camaleón.
* Cerca de 40 items VIP en la DB + NPC que los vende.
* Casas.
* Cercados.
* Recaudadores (90% solo con nombre undefined).
* Captura de almas.
* Invocar mobs.
* Títulos.
* Oficios.
* Solucionado error de conexión interrumpida cuando el servidor tiene lag.
* Nivel máximo de personaje configurable.
* Objetivos (alvos en PT) que dan experiencia.
* Comandos para todos los territorios.
* Nuevos comandos para el jugador: .enchervida y .staff.
* Nuevos comandos para la consola: 
- block (solo los administradores pueden conectarse al servidor).
- lock (libera el servidor).
- shutdown (vuelve a abrir el servidor a la hora deseada).
- trigger (agrega triggers sin tener que hacerlo desde la DB).
* Emulador portugués.

# Créditos

El emulador fue desarrollado originalmente por EduardoLBS (eduardo.lbs@live.com) y Samuka basándose en el emulador Elbustemu. 

Si lo deseas, puedes visitar la página oficial de los desarrolladores haciendo [clic aquí](http://www.lesguardiansemu.xpg.com.br/).
