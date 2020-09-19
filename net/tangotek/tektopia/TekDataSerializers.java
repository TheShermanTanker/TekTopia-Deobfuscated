/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.datasync.DataParameter;
/*    */ import net.minecraft.network.datasync.DataSerializer;
/*    */ import net.minecraft.network.datasync.DataSerializers;
/*    */ 
/*    */ 
/*    */ public class TekDataSerializers
/*    */ {
/* 14 */   public static final DataSerializer<List<Integer>> INT_LIST = new DataSerializer<List<Integer>>()
/*    */     {
/*    */       public void write(PacketBuffer buf, List<Integer> list)
/*    */       {
/* 18 */         buf.writeVarInt(list.size());
/* 19 */         list.stream().forEach(i -> buf.writeVarInt(i.intValue()));
/*    */       }
/*    */       
/*    */       public List<Integer> read(PacketBuffer buf) throws IOException {
/* 23 */         int i = buf.readVarInt();
/* 24 */         List<Integer> outList = new ArrayList<>();
/* 25 */         for (int j = 0; j < i; j++) {
/* 26 */           outList.add(Integer.valueOf(buf.readVarInt()));
/*    */         }
/* 28 */         return outList;
/*    */       }
/*    */       
/*    */       public DataParameter<List<Integer>> createKey(int id) {
/* 32 */         return new DataParameter(id, this);
/*    */       }
/*    */       
/*    */       public List<Integer> copyValue(List<Integer> value) {
/* 36 */         return value;
/*    */       }
/*    */     };
/*    */   
/*    */   static {
/* 41 */     DataSerializers.registerSerializer(INT_LIST);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\TekDataSerializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */