<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/DMemberSubmitOrderReply">
	<DMemberSubmitOrderReply>
	    <ErrorCode><xsl:value-of select="@ErrorCode"></xsl:value-of></ErrorCode>
	    <ErrorMessage><xsl:value-of select="@ErrorMessage"></xsl:value-of></ErrorMessage>
			<Order>
				<voucher>
					<printCode><xsl:value-of select="Order/@PrintNo" /></printCode>
					<verifyCode><xsl:value-of select="Order/@VerifyCode" /></verifyCode>
				</voucher>
				<orderItems>
					<xsl:for-each select="Order/Seat">
						<orderItem>
							<ticketCode><xsl:value-of select="@FilmTicketCode" /></ticketCode>
							<seatCode><xsl:value-of select="@SeatCode" /></seatCode>
						</orderItem>
					</xsl:for-each>
				</orderItems>
			</Order>
	</DMemberSubmitOrderReply>
	</xsl:template>
</xsl:stylesheet>