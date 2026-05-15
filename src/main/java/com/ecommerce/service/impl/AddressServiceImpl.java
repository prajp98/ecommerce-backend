package com.ecommerce.service.impl;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ForbiddenOperationException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository,
                              AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    @Transactional
    public AddressResponse addAddress(String userEmail, AddressRequest request) {
        User user = getUserByEmail(userEmail);

        if (request.isDefaultAddress()) {
            unsetExistingDefaultAddress(user.getId());
        }

        Address address = new Address();
        address.setUser(user);
        address.setLine1(request.getLine1().trim());
        address.setLine2(request.getLine2() == null ? null : request.getLine2().trim());
        address.setCity(request.getCity().trim());
        address.setState(request.getState().trim());
        address.setZipCode(request.getZipCode().trim());
        address.setCountry(request.getCountry().trim());
        address.setDefaultAddress(request.isDefaultAddress());

        Address savedAddress = addressRepository.save(address);
        return buildResponse(savedAddress, "Address added successfully");
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(String userEmail, Long addressId, AddressRequest request) {
        User user = getUserByEmail(userEmail);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot update another user's address");
        }

        if (request.isDefaultAddress()) {
            unsetExistingDefaultAddress(user.getId());
        }

        address.setLine1(request.getLine1().trim());
        address.setLine2(request.getLine2() == null ? null : request.getLine2().trim());
        address.setCity(request.getCity().trim());
        address.setState(request.getState().trim());
        address.setZipCode(request.getZipCode().trim());
        address.setCountry(request.getCountry().trim());
        address.setDefaultAddress(request.isDefaultAddress());

        Address updatedAddress = addressRepository.save(address);
        return buildResponse(updatedAddress, "Address updated successfully");
    }

    @Override
    @Transactional
    public AddressResponse deleteAddress(String userEmail, Long addressId) {
        User user = getUserByEmail(userEmail);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot delete another user's address");
        }

        AddressResponse response = buildResponse(address, "Address deleted successfully");
        addressRepository.delete(address);
        return response;
    }

    @Override
    public List<AddressResponse> getMyAddresses(String userEmail) {
        User user = getUserByEmail(userEmail);

        return addressRepository.findByUserId(user.getId())
                .stream()
                .map(address -> buildResponse(address, "Address fetched successfully"))
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(String userEmail, Long addressId) {
        User user = getUserByEmail(userEmail);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot update another user's address");
        }

        unsetExistingDefaultAddress(user.getId());

        address.setDefaultAddress(true);
        Address updatedAddress = addressRepository.save(address);
        return buildResponse(updatedAddress, "Default address updated successfully");
    }

    private AddressResponse buildResponse(Address address, String message) {
        AddressResponse response = addressMapper.toResponse(address);
        response.setMessage(message);
        return response;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private void unsetExistingDefaultAddress(Long userId) {
        addressRepository.findByUserIdAndDefaultAddressTrue(userId)
                .ifPresent(existingDefault -> {
                    existingDefault.setDefaultAddress(false);
                    addressRepository.save(existingDefault);
                });
    }
}