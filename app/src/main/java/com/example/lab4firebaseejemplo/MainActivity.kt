package com.example.lab4firebaseejemplo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class MainActivity : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    lateinit var jsonText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dbRef = Firebase.database.reference
        jsonText = findViewById<TextView>(R.id.jsonText)
        //agregar()
//        modificar()
//        eliminar()
//        listar()
//        listarSinEscucharCambios()
        listarEscuchandoCiertosCambios()
    }

    fun eliminar(){

        dbRef.child("paises").child("-NvkrRFTAh3FxqS-noCo").removeValue()

    }

    val paisesList = mutableListOf<Pais>()

    fun listarSinEscucharCambios(){
        dbRef.child("paises").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                paisesList.clear()
                var paisesText = ""
                for (snap in dataSnapshot.children) {
                    val m = snap.getValue(Pais::class.java)
                    if(m != null){
                        paisesList.add(m)
                        paisesText += "${m?.nombre}\n"
                    }

                }
                jsonText.text = paisesText
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar la cancelación, si es necesario
            }
        })
    }

    fun listarEscuchandoCiertosCambios(){
        paisesList.clear()
        dbRef.child("paises").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val m = dataSnapshot.getValue(Pais::class.java)
                if(m != null){
                    paisesList.add(m)
                    jsonText.text = jsonText.text.toString()+"${m.nombre}\n"
                }

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val m = dataSnapshot.getValue(Pais::class.java)
                Toast.makeText(applicationContext, "Se actualizó ${m?.nombre}", Toast.LENGTH_SHORT).show()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Toast.makeText(applicationContext, "Se eliminó", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Toast.makeText(applicationContext, "Se ordenó", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar la cancelación, si es necesario
            }
        })
    }


    fun listar(){
        val paisesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val post = dataSnapshot.getValue()
                var paisesText = ""
                for (snapshot in dataSnapshot.children){
                    val p = snapshot.getValue(Pais::class.java)

                    if (p != null) {
                        Log.i("TEOTEOLS","AGREGANDO A LA LISTA "+p.nombre)
                        paisesList.add(p)
                        paisesText += "${p?.nombre}\n"
                    }

                    //Log.i("TEOTEOLS",snapshot.key.toString())
//                    val p = snapshot.child(snapshot.key.toString()).value
//                    Log.i("TEOTEOLS",p.toString())
                }
                jsonText.text = paisesText




            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("TEOTEOLS","CANCELED")
            }
        }
        dbRef.child("paises").addValueEventListener(paisesListener)
    }

    fun modificar(){
        val m:Pais = Pais("-NvkrVZl_vsKp3cAjti-","PERU MODIFICADO","A")
        dbRef.child("paises").child(m.id).setValue(m).addOnSuccessListener {
            Log.i("TEOTEO","Se actualizo correctamente")

        }.addOnFailureListener {
            Log.i("TEOTEO","Se produjo un error al actualizar "+it.message)

        }

    }


    fun agregar(){
        val id: String? = dbRef.push().key
        val m:Pais = Pais(id!!,"PERU","A")
        dbRef.child("paises").child(id).setValue(m).addOnSuccessListener {
            Log.i("TEOTEO","Se agrego correctamente")

        }.addOnFailureListener {
            Log.i("TEOTEO","Se produjo un error al agregar "+it.message)

        }

    }


}