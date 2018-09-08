package com.task.rest.resource;

import com.task.rest.api.request.CreateAccountRequest;
import com.task.rest.api.request.DepositOrWithdrawRequest;
import com.task.rest.api.request.TransferRequest;
import com.task.rest.api.response.CreateOrUpdateAccountResponse;
import com.task.rest.api.response.ListAllResponse;
import com.task.rest.model.dbo.Account;
import com.task.rest.service.AccountService;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Alexander Kotov (kotov.alex.22@gmail.com)
 */

@Path("/account-service")
@Produces(MediaType.APPLICATION_JSON)
public class AccountServiceResource {

    private final AccountService accountService;

    private final BigDecimal initialMoneyAmount;

    public AccountServiceResource(AccountService accountService, BigDecimal initialMoneyAmount) {
        this.accountService = accountService;
        this.initialMoneyAmount = initialMoneyAmount;
    }

    @POST
    @UnitOfWork
    @Path("/create")
    public Response create(@Valid @NotNull CreateAccountRequest request) {
        final Account account = accountService.create(new Account(Optional.ofNullable(request.getInitAmount()).orElse(initialMoneyAmount)));
        return Response.status(Response.Status.CREATED)
                .entity(new CreateOrUpdateAccountResponse(account.getId(), account.getAmount()))
                .build();
    }

    @GET
    @UnitOfWork
    @Path("/list")
    public Response listAll() {
        List<Account> accounts = accountService.listAll();
        return Response.status(Response.Status.OK)
                .entity(new ListAllResponse(accounts))
                .build();
    }

    @POST
    @UnitOfWork
    @Path("/withdraw")
    public Response withdraw(@Valid @NotNull DepositOrWithdrawRequest request) {
        final Account account = accountService.withdraw(request.getId(), request.getAmount());
        return Response.status(Response.Status.OK)
                .entity(new CreateOrUpdateAccountResponse(account.getId(), account.getAmount()))
                .build();
    }

    @POST
    @UnitOfWork
    @Path("/deposit")
    public Response deposit(@Valid @NotNull DepositOrWithdrawRequest request) {
        final Account account = accountService.deposit(request.getId(), request.getAmount());
        return Response.status(Response.Status.OK)
                .entity(new CreateOrUpdateAccountResponse(account.getId(), account.getAmount()))
                .build();
    }

    @POST
    @UnitOfWork
    @Path("/transfer")
    public Response deposit(@Valid @NotNull TransferRequest request) {
        final Account account = accountService.transfer(request.getFromId(), request.getToId(), request.getAmount());
        return Response.status(Response.Status.OK)
                .entity(new CreateOrUpdateAccountResponse(account.getId(), account.getAmount()))
                .build();
    }
}
