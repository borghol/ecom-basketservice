# Overview
The Basket Service provides an API for creating, revising, and removing items from a User’s basket.

## Capabilities
- Users will be able to store their cart and count of each item. 
- Users will be able to add/remove items from the cart
- Users will be able to modify the count of each item in the cart
- Users will be able to clear their cart
- Users will be able to see the number of their cart items on the navbar
- Users will be able to store their wishlist
- Users will be able to move items from their wishlist to their basket
- Users should be able to add/remove items from their wishlist
- The basket should be limited to store a specified number of items
- The basket should be limited to store a specific measurement size
- Basket is cleared when the user generates an order
- Items stay in the basket for 30 days, then they are moved to the wishlist
- A visitor logging in will merge baskets

## Service API
| Commands                  | Queries                 | Events
| -                         | -                       | -
| `saveToBasket()`          | `getCart()`             | Cart Item Added
| `removeFromBasket()`      | `getCartCount()`        | Cart Item Removed
| `clearCart()`             |                         | Cart Item Edited
|                           |                         | Cart Cleared

## Non-Functional Requirements
- The system should be able to store 10M baskets.
- The latency to access a basket should be:
    - P90 – 50ms
    - P99 – 100ms
    - P100 – 500ms
- The baskets history needs to be stored in DWH (redshift).

## Observability

### Key Metrics
- items_added_to_basket
- checked_out_baskets
- baskets_cleared
- users_logged_in
- items_removed_from_basket
- items_moved_to_wishlist

### Health Check Endpoint
    /actuator/health


# Implementation

## System Architecture

![System Architecture](/docs/system-architecture.png)

## Database Queries

- `findItemsByUserId()`
- `findItemByUserIdAndItemId()`
- `saveItem()`
- `deleteItem()`

## Primary Database Schema

### Partition Key
- `UserPrefix_UserId`
    - **UserPrefix**: Identifies type of User. (OMNI / VISI)
    - **UserId**: Account User ID or user session ID is not logged in.

### Sort Key
- `ItemId`

### Attributes
- `Quantity`
- `QuantityUnits`
- `UserId`
- `UserType`
- `CreatedBy`
- `CreatedAt`
- `UpdatedBy`
- `UpdatedAt`
- `IPAddress`
- `isDeleted`


| Partiton Key | Sort Key        | Attributes | | | | | | | | |
| - | - | - | - | - | - | - | - | - | - | -
| UserPrefix_UserId | ItemId | Quantity | QuantityUnits | UserId | CreatedBy | CreatedAt | UpdatedBy | UpdatedAt | IPAddress | isDeleted


## Dependencies

### Invokes
- **Wishlist Service**
    - `saveItem()`

### Subscribes
- **Catalog Service**
    - Item Removed

- **Purchase Service**
    - Order Placed
    
- **Authentication Service**
    - Authorization Token
    - Session ID
