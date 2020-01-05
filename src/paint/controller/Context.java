/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint.controller;

/**
 *
 * @author Ahmed
 */
public class Context {
       FilesManagement reader ;

   public Context(FilesManagement reader) {
    super();
    this.reader = reader;
   }

   public void executeWrite(){

        reader.write();
    }
   public void executeRead(){
       reader.read();
   }

    
}
