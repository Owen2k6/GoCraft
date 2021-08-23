package minecraft.net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapData extends MapDataBase {
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;
    public byte[] colors = new byte[16384];
    public int step;
    public List playersArrayList = new ArrayList();
    private Map playersHashMap = new HashMap();
    public List playersVisibleOnMap = new ArrayList();

    public MapData(String var1) {
        super(var1);
    }

    public void readFromNBT(NBTTagCompound var1) {
        this.dimension = var1.getByte("dimension");
        this.xCenter = var1.getInteger("xCenter");
        this.zCenter = var1.getInteger("zCenter");
        this.scale = var1.getByte("scale");
        if (this.scale < 0) {
            this.scale = 0;
        }

        if (this.scale > 4) {
            this.scale = 4;
        }

        short var2 = var1.getShort("width");
        short var3 = var1.getShort("height");
        if (var2 == 128 && var3 == 128) {
            this.colors = var1.getByteArray("colors");
        } else {
            byte[] var4 = var1.getByteArray("colors");
            this.colors = new byte[16384];
            int var5 = (128 - var2) / 2;
            int var6 = (128 - var3) / 2;

            for(int var7 = 0; var7 < var3; ++var7) {
                int var8 = var7 + var6;
                if (var8 >= 0 || var8 < 128) {
                    for(int var9 = 0; var9 < var2; ++var9) {
                        int var10 = var9 + var5;
                        if (var10 >= 0 || var10 < 128) {
                            this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                        }
                    }
                }
            }
        }

    }

    public void writeToNBT(NBTTagCompound var1) {
        var1.setByte("dimension", this.dimension);
        var1.setInteger("xCenter", this.xCenter);
        var1.setInteger("zCenter", this.zCenter);
        var1.setByte("scale", this.scale);
        var1.setShort("width", (short)128);
        var1.setShort("height", (short)128);
        var1.setByteArray("colors", this.colors);
    }

    public void updateVisiblePlayers(EntityPlayer var1, ItemStack var2) {
        if (!this.playersHashMap.containsKey(var1)) {
            MapInfo var3 = new MapInfo(this, var1);
            this.playersHashMap.put(var1, var3);
            this.playersArrayList.add(var3);
        }

        this.playersVisibleOnMap.clear();

        for(int var14 = 0; var14 < this.playersArrayList.size(); ++var14) {
            MapInfo var4 = (MapInfo)this.playersArrayList.get(var14);
            if (!var4.entityplayerObj.isDead && var4.entityplayerObj.inventory.hasItemStack(var2)) {
                float var5 = (float)(var4.entityplayerObj.posX - (double)this.xCenter) / (float)(1 << this.scale);
                float var6 = (float)(var4.entityplayerObj.posZ - (double)this.zCenter) / (float)(1 << this.scale);
                byte var7 = 64;
                byte var8 = 64;
                if (var5 >= (float)(-var7) && var6 >= (float)(-var8) && var5 <= (float)var7 && var6 <= (float)var8) {
                    byte var9 = 0;
                    byte var10 = (byte)((int)((double)(var5 * 2.0F) + 0.5D));
                    byte var11 = (byte)((int)((double)(var6 * 2.0F) + 0.5D));
                    byte var12 = (byte)((int)((double)(var1.rotationYaw * 16.0F / 360.0F) + 0.5D));
                    if (this.dimension < 0) {
                        int var13 = this.step / 10;
                        var12 = (byte)(var13 * var13 * 34187121 + var13 * 121 >> 15 & 15);
                    }

                    if (var4.entityplayerObj.dimension == this.dimension) {
                        this.playersVisibleOnMap.add(new MapCoord(this, var9, var10, var11, var12));
                    }
                }
            } else {
                this.playersHashMap.remove(var4.entityplayerObj);
                this.playersArrayList.remove(var4);
            }
        }

    }

    public void setColumnDirty(int var1, int var2, int var3) {
        super.markDirty();

        for(int var4 = 0; var4 < this.playersArrayList.size(); ++var4) {
            MapInfo var5 = (MapInfo)this.playersArrayList.get(var4);
            if (var5.field_28119_b[var1] < 0 || var5.field_28119_b[var1] > var2) {
                var5.field_28119_b[var1] = var2;
            }

            if (var5.field_28124_c[var1] < 0 || var5.field_28124_c[var1] < var3) {
                var5.field_28124_c[var1] = var3;
            }
        }

    }

    public void updateMPMapData(byte[] var1) {
        int var2;
        if (var1[0] == 0) {
            var2 = var1[1] & 255;
            int var3 = var1[2] & 255;

            for(int var4 = 0; var4 < var1.length - 3; ++var4) {
                this.colors[(var4 + var3) * 128 + var2] = var1[var4 + 3];
            }

            this.markDirty();
        } else if (var1[0] == 1) {
            this.playersVisibleOnMap.clear();

            for(var2 = 0; var2 < (var1.length - 1) / 3; ++var2) {
                byte var7 = (byte)(var1[var2 * 3 + 1] % 16);
                byte var8 = var1[var2 * 3 + 2];
                byte var5 = var1[var2 * 3 + 3];
                byte var6 = (byte)(var1[var2 * 3 + 1] / 16);
                this.playersVisibleOnMap.add(new MapCoord(this, var7, var8, var5, var6));
            }
        }

    }
}
