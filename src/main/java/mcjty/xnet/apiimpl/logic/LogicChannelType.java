package mcjty.xnet.apiimpl.logic;

import mcjty.xnet.api.channels.IChannelSettings;
import mcjty.xnet.api.channels.IChannelType;
import mcjty.xnet.api.channels.IConnectorSettings;
import mcjty.xnet.apiimpl.fluids.FluidChannelSettings;
import mcjty.xnet.apiimpl.fluids.FluidConnectorSettings;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LogicChannelType implements IChannelType {

    @Override
    public String getID() {
        return "xnet.logic";
    }

    @Override
    public String getName() {
        return "Logic";
    }

    @Override
    public boolean supportsBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Override
    @Nonnull
    public IConnectorSettings createConnector(boolean advanced, @Nonnull EnumFacing side) {
        return new LogicConnectorSettings(advanced, side);
    }

    @Nonnull
    @Override
    public IChannelSettings createChannel() {
        return new LogicChannelSettings();
    }
}
