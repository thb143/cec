<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/payDetail">
		<data>
			<ticketDiscounts>
				<xsl:for-each select="items">
					<ticketDiscount>
						<seatNo><xsl:value-of select="seatId" /></seatNo>
						<ticketPrice><xsl:value-of select="discountPrice" /></ticketPrice>
					</ticketDiscount>
				</xsl:for-each>
			</ticketDiscounts>
			<cinemaOrderCode><xsl:value-of select="orderNo" /></cinemaOrderCode>
			<responseCode><xsl:value-of select="responseCode" /></responseCode>
			<responseMsg><xsl:value-of select="responseMsg" /></responseMsg>
		</data>
	</xsl:template>
</xsl:stylesheet>