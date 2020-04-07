/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package produk;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.UpdateResult;
import java.util.Date;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Nayo
 */
public class TestDB {

    public static void main(String args[]) {
        try {
            // Koneksi ke database mongodb
            MongoDatabase database = Koneksi.sambungDB();
            // melihat daftar koleksi (tabel)
            System.out.println("================================");
            System.out.println("Daftar tabel dalam database");
            MongoIterable<String> tables = database.listCollectionNames();
            for (String name : tables) {
                System.out.println(name);
            }

            // menambah data
            System.out.println("====================================");
            System.out.println("Menambahkan data");
            MongoCollection<Document> col = database.getCollection("produk");
            Document doc = new Document();
            doc.put("Nama", "Printer InkJet");
            doc.put("Harga", 12040000);
            doc.put("Tanggal", new Date());
            col.insertOne(doc);
            System.out.println("Data telah disimpan dalam koleksi");

            // mendapatkan id dari dokumen yang baru saja di insert
            ObjectId id = new ObjectId(doc.get("_id").toString());

            // melihat/menampilkan data
            System.out.println("=============================");
            System.out.println("Data dalam koleksi produk");
            MongoCursor<Document> cursor = col.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }

            // mencari dokumen berdasarkan id
            Document myDoc = col.find(eq("_id", id)).first();
            System.out.println("============================");
            System.out.println("Pencarian data berdasarkan id");
            System.out.println(myDoc.toJson());

            // mengedit data
            Document docs = new Document();
            doc.put("Nama", "Canon");
            Document doc_edit = new Document("$set", docs);
            UpdateResult hasil_edit = col.updateOne(eq("_id", id), doc_edit);
            System.out.println(hasil_edit.getModifiedCount());

            // melihat/menampilkan data
            System.out.println("==========================");
            System.out.println("DAta dalam koleksi produk");
            cursor = col.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }

            // menghapus data
            col.deleteOne(eq("_id", id));

            // melihat/menampilkan data
            System.out.println("==========================");
            System.out.println("DAta dalam koleksi produk");
            cursor = col.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }

        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }
}
