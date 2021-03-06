package mindustry.world.consumers;

import arc.struct.*;
import arc.func.*;
import arc.scene.ui.layout.*;
import arc.util.ArcAnnotate.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.Cicon;
import mindustry.world.meta.*;
import mindustry.world.meta.values.*;

import static mindustry.Vars.*;

public class ConsumeItemFilter extends Consume{
    public final @NonNull
    Boolf<Item> filter;

    public ConsumeItemFilter(Boolf<Item> item){
        this.filter = item;
    }

    @Override
    public void applyItemFilter(Bits arr){
        content.items().each(filter, item -> arr.set(item.id));
    }

    @Override
    public ConsumeType type(){
        return ConsumeType.item;
    }

    @Override
    public void build(Tilec tile, Table table){
        MultiReqImage image = new MultiReqImage();
        content.items().each(i -> filter.get(i) && (!state.isCampaign() || data.isUnlocked(i)), item -> image.add(new ReqImage(new ItemImage(item.icon(Cicon.medium), 1),
            () -> tile.items() != null && tile.items().has(item))));

        table.add(image).size(8 * 4);
    }

    @Override
    public String getIcon(){
        return "icon-item";
    }

    @Override
    public void update(Tilec entity){

    }

    @Override
    public void trigger(Tilec entity){
        for(int i = 0; i < content.items().size; i++){
            Item item = content.item(i);
            if(entity.items() != null && entity.items().has(item) && this.filter.get(item)){
                entity.items().remove(item, 1);
                break;
            }
        }
    }

    @Override
    public boolean valid(Tilec entity){
        for(int i = 0; i < content.items().size; i++){
            Item item = content.item(i);
            if(entity.items() != null && entity.items().has(item) && this.filter.get(item)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void display(BlockStats stats){
        stats.add(booster ? BlockStat.booster : BlockStat.input, new ItemFilterValue(filter));
    }
}
