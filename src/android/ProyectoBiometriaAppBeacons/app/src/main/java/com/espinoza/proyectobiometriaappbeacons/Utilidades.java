package com.espinoza.proyectobiometriaappbeacons;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

// -----------------------------------------------------------------------------------
// @author: Jordi Bataller i Mascarell
// -----------------------------------------------------------------------------------
public class Utilidades {

    private static final java.util.Map<Integer, Double> contadoresProcesados = new java.util.HashMap<>();

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static byte[] stringToBytes ( String texto ) {
        return texto.getBytes();
        // byte[] b = string.getBytes(StandardCharsets.UTF_8); // Ja
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static UUID stringToUUID( String uuid ) {
        if ( uuid.length() != 16 ) {
            throw new Error( "stringUUID: string no tiene 16 caracteres ");
        }
        byte[] comoBytes = uuid.getBytes();

        String masSignificativo = uuid.substring(0, 8);
        String menosSignificativo = uuid.substring(8, 16);
        UUID res = new UUID( Utilidades.bytesToLong( masSignificativo.getBytes() ), Utilidades.bytesToLong( menosSignificativo.getBytes() ) );

        // Log.d( MainActivity.ETIQUETA_LOG, " \n\n***** stringToUUID *** " + uuid  + "=?=" + Utilidades.uuidToString( res ) );

        // UUID res = UUID.nameUUIDFromBytes( comoBytes ); no va como quiero

        return res;
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static String uuidToString ( UUID uuid ) {
        return bytesToString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static String uuidToHexString ( UUID uuid ) {
        return bytesToHexString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static String bytesToString( byte[] bytes ) {
        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append( (char) b );
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static byte[] dosLongToBytes( long masSignificativos, long menosSignificativos ) {
        ByteBuffer buffer = ByteBuffer.allocate( 2 * Long.BYTES );
        buffer.putLong( masSignificativos );
        buffer.putLong( menosSignificativos );
        return buffer.array();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static int bytesToInt( byte[] bytes ) {
        return new BigInteger(bytes).intValue();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static long bytesToLong( byte[] bytes ) {
        return new BigInteger(bytes).longValue();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static int bytesToIntOK( byte[] bytes ) {
        if (bytes == null ) {
            return 0;
        }

        if ( bytes.length > 4 ) {
            throw new Error( "demasiados bytes para pasar a int ");
        }
        int res = 0;



        for( byte b : bytes ) {
           /*
           Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): byte: hex=" + Integer.toHexString( b )
                   + " dec=" + b + " bin=" + Integer.toBinaryString( b ) +
                   " hex=" + Byte.toString( b )
           );
           */
            res =  (res << 8) // * 16
                    + (b & 0xFF); // para quedarse con 1 byte (2 cuartetos) de lo que haya en b
        } // for

        if ( (bytes[ 0 ] & 0x8) != 0 ) {
            // si tiene signo negativo (un 1 a la izquierda del primer byte
            res = -(~(byte)res)-1; // complemento a 2 (~) de res pero como byte, -1
        }
       /*
        Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): res = " + res + " ~res=" + (res ^ 0xffff)
                + "~res=" + ~((byte) res)
        );
        */

        return res;
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static String bytesToHexString( byte[] bytes ) {

        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
            sb.append(':');
        }
        return sb.toString();
    } // ()

    // -------------------------------------------------------------------------------
    // Metodo: interpretarTrama()
    // Procesa una trama iBeacon recibida, separa el tipo, el valor y un contador.
    // Evita lecturas duplicadas usando el contador como identificador único.
    // Devuelve un objeto Medida listo para enviar al servidor.
    // -------------------------------------------------------------------------------
    public static Medida interpretarTrama(TramaIBeacon trama) {
        // Convertimos los bytes a enteros para manipularlos
        int major = Utilidades.bytesToInt(trama.getMajor());
        int minor = Utilidades.bytesToInt(trama.getMinor());

        // Extraemos información codificada en los bytes
        int tipoCodificado = (major >> 8) & 0xFF;  // parte alta del major = tipo de sensor
        double valor = minor / 10.0;               // el valor medido, con una escala decimal
        int contador = major & 0xFF;               // parte baja del major = contador incremental

        // Verificamos si ya procesamos este contador con el mismo valor
        if (!contadoresProcesados.containsKey(contador)) {
            // Primera vez que llega este contador → se guarda y devuelve la medida
            contadoresProcesados.put(contador, valor);

            String tipoTexto = (tipoCodificado == 1) ? "CO2" : "Temperatura";
            return new Medida(tipoTexto, valor);
        } else {
            double valorPrevio = contadoresProcesados.get(contador);
            if (valorPrevio != valor) {
                // Mismo contador pero valor distinto → se actualiza
                contadoresProcesados.put(contador, valor);

                String tipoTexto = (tipoCodificado == 1) ? "CO2" : "Temperatura";
                return new Medida(tipoTexto, valor);
            } else {
                // Mismo contador y mismo valor → duplicado, se ignora
                return null;
            }
        }
    }


} // class
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------


