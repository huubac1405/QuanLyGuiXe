package com.example.quanlyguixe.screen.check_in_out_vehicle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyguixe.data.model.Tickets;
import com.example.quanlyguixe.data.model.Vehicle;
import com.example.quanlyguixe.data.repo.TicketRepository;
import com.example.quanlyguixe.data.repo.VehicleRepository;
import com.example.quanlyguixe.util.base.BaseViewModel;
import com.example.quanlyguixe.util.interfaces.IResultListener;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CheckInCheckOutVehicleViewModel extends BaseViewModel {

    private final VehicleRepository vehicleRepository;

    private final TicketRepository ticketRepository;

    private final MutableLiveData<List<Vehicle>> _vehicles = new MutableLiveData<>();
    @Inject
    public CheckInCheckOutVehicleViewModel(
            TicketRepository ticketRepository,
            VehicleRepository vehicleRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.ticketRepository = ticketRepository;
    }

    public LiveData<List<Vehicle>> getVehicles() {
        return _vehicles;
    }

    private final MutableLiveData<Boolean> _isCheckOutComplete = new MutableLiveData<>(false);

    public LiveData<Boolean> isCheckOutComplete() {
        return _isCheckOutComplete;
    }


    private final MutableLiveData<List<Tickets>> _tickets = new MutableLiveData<>();

    public LiveData<List<Tickets>> getTickets() {
        return _tickets;
    }


    public void getAllTickets() {
        registerDisposable(
                executeTaskWithLoading(
                        ticketRepository.getAllTickets(),
                        new IResultListener<List<Tickets>>() {
                            @Override
                            public void onSuccess(List<Tickets> data) {
                                _tickets.setValue(data);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                error.setValue(throwable.getMessage());
                            }
                        }
                )
        );
    }

    public void getAllVehicles() {
        registerDisposable(
                executeTaskWithLoading(
                        vehicleRepository.getAllVehicles(),
                        new IResultListener<List<Vehicle>>() {
                            @Override
                            public void onSuccess(List<Vehicle> data) {
                                _vehicles.setValue(data);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                error.setValue(throwable.getMessage());
                            }
                        }
                )
        );
    }

    public void insertVehicle(Vehicle vehicle) {
        registerDisposable(
                executeTaskWithLoading(
                        vehicleRepository.insertVehicle(vehicle),
                        new IResultListener<Long>() {

                            @Override
                            public void onSuccess(Long data) {
                                _backToPreviousScreen.setValue(true);
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                        }
                )
        );
    }

    public void deleteVehicle(List<Vehicle> vehicle) {
        registerDisposable(
                executeTaskWithLoading(
                        vehicleRepository.deleteVehicle(vehicle),
                        new IResultListener<Integer>() {

                            @Override
                            public void onSuccess(Integer data) {
                                _isCheckOutComplete.setValue(true);
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                        }
                )
        );
    }

    public void resetCompleteState() {
        _isCheckOutComplete.setValue(false);
    }
}