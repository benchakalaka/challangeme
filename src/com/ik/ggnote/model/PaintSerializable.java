package com.ik.ggnote.model;

import java.io.Serializable;

import android.graphics.Paint;

public class PaintSerializable extends Paint implements Serializable {
     private static final long serialVersionUID = 2166800575010102231L;
     public int                position;
     public int                colour;
     public float              brushStrokeWith;

     public PaintSerializable ( int ditherFlag ) {
          super(ditherFlag);
     }

     public PaintSerializable () {
     }

}
