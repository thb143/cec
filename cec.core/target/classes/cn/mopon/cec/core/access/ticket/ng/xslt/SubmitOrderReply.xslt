<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/SubmitSCTSOrderReply/Order">
		<Order>
			<voucher>
				<printCode><xsl:value-of select="@PrintNo" /></printCode>
				<verifyCode><xsl:value-of select="@VerifyCode" /></verifyCode>
			</voucher>
			<orderItems>
				<xsl:for-each select="Seat">
					<orderItem>
						<ticketCode><xsl:value-of select="@FilmTicketCode" /></ticketCode>
						<seatCode><xsl:value-of select="@SeatCode" /></seatCode>
					</orderItem>
				</xsl:for-each>
			</orderItems>
		</Order>
	</xsl:template>
</xsl:stylesheet>