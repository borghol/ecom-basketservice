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
- A visitor logging in will merge both baskets & wishlists

## Service API
| Commands                  | Queries                 | Events
| -                         | -                       | -
| `addtoCart()`             | `getCart()`             | Cart Item Added
| `editItemCount()`         | `getCartCount()`        | Cart Item Removed
| `removeFromCart()`        | `getWishlist()`         | Cart Item Edited
| `clearCart()`             | `getWishlistCount()`    | Wishlist Item Added
| `addToWishlist()`         |                         | Wishlist Item Removed
| `removeFromWishlist()`    |                         | Wishlist Item Moved to Cart
| `moveItemToCart()`        |

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

### Health Check Endpoint
    /actuator/health


# Implementation

## System Architecture

![System Architecture](/docs/system-architecture.png)

## Entity Diagram

![Entity Data Diagram](docs\entity-data-diagram.png)

## Database Queries

- `findAllCartByUserId()`
- `findAllWishlistByUserId()`
- `getCartCountByUserId()`
- `getWishlistCountByUserId()`
- `putItemInCart()`
- `putItemInWishlist()`
- `updateItemByUserIdAndItemId()`
- `clearCart()`
- `clearWishlist()`
- `removeItemFromCart()`
- `removeItemFromWishlist()`

## Primary Database Schema

### Primary Schema

| Partiton Key | Sort Key        | Attributes | | |
| - | - | - | - | -
| UserId       | Location_ItemId | Location | Item Details | User Details


## Dependencies

### Invokes
`None`

### Subscribes
- **Catalog Service**
    - Item Removed

- **Purchase Service**
    - Order Placed
    
- **Authentication Service**
    - Authorization Token
    - Session ID
