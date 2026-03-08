/*
 *     Copyright (c) 2023, xwjcool.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cool.xwj.blocktuner.mixin;

import cool.xwj.blocktuner.TuningScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NoteBlock.class)
public class NoteBlockMixinClient extends Block {

    public NoteBlockMixinClient(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack stack = super.getPickStack(world, pos, state);
        if (Screen.hasControlDown()) {
            copyBlockState(state, stack);
        }
        return stack;
    }

    @Unique
    private static void copyBlockState(BlockState state, ItemStack stack) {
        stack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(NoteBlock.NOTE, state.get(NoteBlock.NOTE)));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (placer != null && client != null && placer == client.player && Screen.hasControlDown()) {
            client.execute(() -> client.setScreen(new TuningScreen(Text.empty(), pos)));
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }
}
