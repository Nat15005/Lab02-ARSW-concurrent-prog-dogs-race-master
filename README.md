Escuela Colombiana de Ingeniería

Arquitecturas de Software – ARSW

#### Desarrollado por Laura Natalia Rojas Robayo y Ana Maria Duran.

## Taller – programación concurrente, condiciones de carrera y sincronización de hilos.

### Parte I – Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.

    ![image](https://github.com/user-attachments/assets/48a49e79-dd5f-4301-9fee-7d8f4588611b)

2. Modifique el programa para que, en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.

    ![image](https://github.com/user-attachments/assets/0833437e-63a8-4f22-a8a6-6d96a73f3dec)

3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.



### Parte II 


Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

Taller.

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.

    - Primero que todo vemos como al iniciar el programa se muestra el ganador antes de haber finalizado la carrera
      
      ![image](https://github.com/user-attachments/assets/8eb83e11-4b6c-498b-8ca5-04cb1c4d2911)
      
    - Para solucionar esto, hacemos uso del join(), que se encargará de que el hilo principal se asegure que todos los hilos 'galgo' hayan terminado antes de mostrar el resultado
      
      ![image](https://github.com/user-attachments/assets/12aec91d-0eeb-40c0-aa01-360d10c3e4f3)

    - Ahora al iniciar, no se muestra el mensaje antes de tiempo

      ![image](https://github.com/user-attachments/assets/98520d7d-3fd8-432e-9a59-b151d3e92fa2)

      ![image](https://github.com/user-attachments/assets/7f13c304-f60c-45da-8e46-794fd4b2cd0b)


2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.

    - Al correr el programa identificamos que hay inconsistencias en el resultado, ya que a diferentes galgos se le asigna la misma posición. Esto indica que se está presentando una condición de carrera

      ![image](https://github.com/user-attachments/assets/327b21f2-6093-47ca-99dd-8688926a3b3e)

    - Ahora, identificamos las regiones críticas del programa. El problema radica en que varios hilos pueden leer el valor de ultimaPosicionAlcanzada simultáneamente antes de que cualquiera de ellos lo modifique, lo que provoca que varios galgos piensen que han alcanzado la misma posición.

      ![image](https://github.com/user-attachments/assets/16639b91-fe8b-43e3-9056-7d0fae55e395)

3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.

    - Utilizamos synchronized para garantizar que a dichas regiones críticas solo acceda un hilo a la vez

      ![image](https://github.com/user-attachments/assets/3ef4f834-293f-4194-ad41-3ef0ea1b3321)


4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).

    - Para Implementar las funcionalidades de pausa y continuar, agregamos una bandera, para indicar si la bandera está en pausa y un monitor, para sincronizar los hilos

      ![image](https://github.com/user-attachments/assets/044592fe-f1a6-41af-aad4-e2a9730244ee)

    - Los hilos verifican el estado de paused y, si está activado, entran en espera utilizando monitor.wait().

      ![image](https://github.com/user-attachments/assets/5a7d8f01-5914-4f84-a78b-0fabcb2f199c)

    - Creamos métodos para pausar y reanudar la carrera

      ![image](https://github.com/user-attachments/assets/ccbc9760-8ff3-4903-92ea-01d453d04a8d)

    - Modificamos la acción del botón Stop y Continue para que se invoquen los métodos Galgo.pauseRace() y Galgo.resumeRace() respectivamente.

      ![image](https://github.com/user-attachments/assets/af9b5fee-6494-40bc-9cd7-2bbcff1d775e)

## Criterios de evaluación

1. Funcionalidad.

    1.1. La ejecución de los galgos puede ser detenida y resumida consistentemente.
    
    1.2. No hay inconsistencias en el orden de llegada registrado.
    
2. Diseño.   

    2.1. Se hace una sincronización de sólo la región crítica (sincronizar, por ejemplo, todo un método, bloquearía más de lo necesario).
    
    2.2. Los galgos, cuando están suspendidos, son reactivados son sólo un llamado (usando un monitor común).

