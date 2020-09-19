/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
/*     */ import net.minecraftforge.registries.IForgeRegistry;
/*     */ import net.minecraftforge.registries.IForgeRegistryEntry;
/*     */ 
/*     */ 
/*     */ @ObjectHolder("tektopia")
/*     */ public class ModSoundEvents
/*     */ {
/*  17 */   public static final SoundEvent villagerSleep = createSoundEvent("villager_sleep");
/*  18 */   public static final SoundEvent villagerAfraid = createSoundEvent("villager_afraid");
/*  19 */   public static final SoundEvent villagerGrunt = createSoundEvent("villager_grunt");
/*  20 */   public static final SoundEvent villagerEnchant = createSoundEvent("villager_enchant");
/*  21 */   public static final SoundEvent villagerEnchantApply = createSoundEvent("villager_enchant_apply");
/*  22 */   public static final SoundEvent villagerAngry = createSoundEvent("villager_angry");
/*  23 */   public static final SoundEvent villagerHappy = createSoundEvent("villager_happy");
/*  24 */   public static final SoundEvent villagerSocialize = createSoundEvent("villager_socialize");
/*  25 */   public static final SoundEvent villagerHeartMagic = createSoundEvent("villager_heart_magic");
/*  26 */   public static final SoundEvent twinkle = createSoundEvent("twinkle");
/*  27 */   public static final SoundEvent healingSource = createSoundEvent("healing_source");
/*  28 */   public static final SoundEvent healingTarget = createSoundEvent("healing_target");
/*  29 */   public static final SoundEvent earthRumble = createSoundEvent("earth_rumble");
/*  30 */   public static final SoundEvent earthBlast = createSoundEvent("earth_blast");
/*  31 */   public static final SoundEvent deathCircle = createSoundEvent("death_circle");
/*  32 */   public static final SoundEvent deathSummon = createSoundEvent("death_summon");
/*  33 */   public static final SoundEvent deathSummonTarget = createSoundEvent("death_summon_target");
/*  34 */   public static final SoundEvent deathSummonEnd = createSoundEvent("death_summon_end");
/*  35 */   public static final SoundEvent deathSkullLeave = createSoundEvent("death_skull_leave");
/*  36 */   public static final SoundEvent deathSkullArrive = createSoundEvent("death_skull_arrive");
/*  37 */   public static final SoundEvent deathSkullRebound = createSoundEvent("death_skull_rebound");
/*  38 */   public static final SoundEvent deathShield = createSoundEvent("death_shield");
/*  39 */   public static final SoundEvent deathFullSkulls = createSoundEvent("death_full_skulls");
/*  40 */   public static final SoundEvent necroDead = createSoundEvent("necro_dead");
/*  41 */   public static final SoundEvent slamGround = createSoundEvent("slam_ground");
/*  42 */   public static final SoundEvent bigAttack = createSoundEvent("big_attack");
/*  43 */   public static final SoundEvent courageAura = createSoundEvent("courage_aura");
/*     */ 
/*     */   
/*  46 */   public static final SoundEvent fluteShort1 = createSoundEvent("flute_short_1");
/*  47 */   public static final SoundEvent fluteShort2 = createSoundEvent("flute_short_2");
/*  48 */   public static final SoundEvent fluteShort3 = createSoundEvent("flute_short_3");
/*  49 */   public static final SoundEvent fluteShort4 = createSoundEvent("flute_short_4");
/*  50 */   public static final SoundEvent fluteShort5 = createSoundEvent("flute_short_5");
/*  51 */   public static final SoundEvent fluteShort6 = createSoundEvent("flute_short_6");
/*  52 */   public static final SoundEvent fluteShort7 = createSoundEvent("flute_short_7");
/*  53 */   public static final SoundEvent fluteTavern1 = createSoundEvent("flute_tavern_1");
/*  54 */   public static final SoundEvent fluteTavern2 = createSoundEvent("flute_tavern_2");
/*  55 */   public static final SoundEvent fluteTavern3 = createSoundEvent("flute_tavern_3");
/*     */   
/*     */   public enum Performance
/*     */   {
/*  59 */     FluteShort1((byte)1, ModSoundEvents.fluteShort1, false, 204, "villager_flute_1"),
/*  60 */     FluteShort2((byte)2, ModSoundEvents.fluteShort2, false, 190, "villager_flute_1"),
/*  61 */     FluteShort3((byte)3, ModSoundEvents.fluteShort3, false, 140, "villager_flute_1"),
/*  62 */     FluteShort4((byte)4, ModSoundEvents.fluteShort4, false, 92, "villager_flute_1"),
/*  63 */     FluteShort5((byte)5, ModSoundEvents.fluteShort5, false, 184, "villager_flute_1"),
/*  64 */     FluteShort6((byte)6, ModSoundEvents.fluteShort6, false, 313, "villager_flute_1"),
/*  65 */     FluteShort7((byte)7, ModSoundEvents.fluteShort7, false, 112, "villager_flute_1"),
/*  66 */     FluteTavern1((byte)8, ModSoundEvents.fluteTavern1, true, 666, "villager_flute_1"),
/*  67 */     FluteTavern2((byte)9, ModSoundEvents.fluteTavern2, true, 764, "villager_flute_1"),
/*  68 */     FluteTavern3((byte)10, ModSoundEvents.fluteTavern3, true, 880, "villager_flute_1");
/*     */     
/*     */     Performance(byte id, SoundEvent sound, boolean tavern, int duration, String anim) {
/*  71 */       this.duration = duration;
/*  72 */       this.id = id;
/*  73 */       this.anim = anim;
/*  74 */       this.inTavern = tavern;
/*  75 */       this.sound = sound;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final SoundEvent sound;
/*     */ 
/*     */     
/*     */     public final int duration;
/*     */     
/*     */     public final byte id;
/*     */     
/*     */     public final String anim;
/*     */     
/*     */     public final boolean inTavern;
/*     */ 
/*     */     
/*     */     public static Performance getRandom(boolean tav, Random rnd) {
/*  93 */       List<Performance> perfs = (List<Performance>)Arrays.<Performance>stream(values()).filter(p -> (p.inTavern == tav)).collect(Collectors.toList());
/*  94 */       if (!perfs.isEmpty()) {
/*  95 */         return perfs.get(rnd.nextInt(perfs.size()));
/*     */       }
/*     */       
/*  98 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerSounds(IForgeRegistry<SoundEvent> registry) {
/* 107 */     registry.registerAll((IForgeRegistryEntry[])new SoundEvent[] { villagerSleep, villagerAfraid, villagerGrunt, villagerEnchant, villagerEnchantApply, villagerAngry, villagerHappy, villagerHeartMagic, twinkle, healingSource, healingTarget, earthRumble, earthBlast, deathCircle, deathSummon, deathSummonTarget, deathSkullArrive, deathSkullLeave, deathSkullRebound, deathShield, deathFullSkulls, deathSummonEnd, necroDead, slamGround, bigAttack, courageAura });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     Arrays.<Performance>stream(Performance.values()).map(p -> p.sound).forEach(s -> registry.register((IForgeRegistryEntry)s));
/*     */   }
/*     */   
/*     */   private static SoundEvent createSoundEvent(String soundName) {
/* 141 */     ResourceLocation soundID = new ResourceLocation("tektopia", soundName);
/* 142 */     return (SoundEvent)(new SoundEvent(soundID)).setRegistryName(soundID);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ModSoundEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */